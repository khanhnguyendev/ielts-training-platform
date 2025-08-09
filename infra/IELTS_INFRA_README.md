# IELTS Infra — GitOps Bootstrap README

Generated: 2025-08-09T16:28:25

This repository bootstraps a **self-managed Kubernetes** cluster using **Argo CD (GitOps)** and
centralizes deployments for your apps and add‑ons (Ingress, TLS, External Secrets, Monitoring).

---

## ✅ What you’ll get after completing this guide
- Argo CD installed and syncing this repo
- Ingress controller, cert-manager, external-secrets, and monitoring stack (Prometheus/Grafana)
- Your **frontend** app deployed from an image you pushed (Docker Hub/GHCR)
- Clean layout ready to add backend services (gateway, scoring, etc.)

---

## 1) Prerequisites
- A running Kubernetes cluster (k3s/kubeadm). Tested with **k3s 1.28+**.
- `kubectl` access on your machine
- A container registry (Docker Hub or GHCR) with a pushed image (e.g. `your-dockerhub-user/ielts-frontend:dev-001`)
- A domain or subdomain for the app, e.g. `app.dev.example.com` (optional but recommended)

> **Tip:** On a single VPS with k3s, the Ingress service will usually expose your VPS public IP.
> Point your DNS `A` record at that IP once the Ingress controller is up.

---

## 2) Repository layout

```
k8s/
  clusters/
    dev/
      root-application.yaml      # Argo CD "app of apps" entrypoint for dev (recurse the folder below)
  apps/
    dev/
      addons/
        ingress-nginx.yaml       # Ingress controller via Helm (Argo Application)
        cert-manager.yaml        # cert-manager via Helm (CRDs enabled)
        external-secrets.yaml    # External Secrets Operator via Helm
        monitoring.yaml          # kube-prometheus-stack (Prometheus+Grafana)
        values/monitoring-values.yaml
        cert-manager/clusterissuer-staging.yaml   # sample ACME ClusterIssuer
        external-secrets/store-sample.yaml        # sample ClusterSecretStore (AWS)
      frontend.yaml              # Argo Application pointing to the frontend dev overlay
    frontend/
      base/                      # Kustomize base: ns, deployment, service, ingress
        namespace.yaml
        deployment.yaml
        service.yaml
        ingress.yaml
        kustomization.yaml
      overlays/
        dev/
          kustomization.yaml     # sets image name/tag and any env overrides
.github/workflows/
  lint-k8s.yml                   # CI: kustomize build + kubeconform schema validation
```

**How it wires together**
- `clusters/dev/root-application.yaml` → tells Argo CD to **recurse** `k8s/apps/dev`.
- Everything in `k8s/apps/dev` is an **Argo CD Application**:
  - add‑ons (Helm charts)
  - `frontend.yaml` → points to `k8s/apps/frontend/overlays/dev` (your app manifests).

---

## 3) One-time: install Argo CD

```bash
# 3.1 Create namespace
kubectl create namespace argocd

# 3.2 Install official Argo CD manifests
kubectl apply -n argocd -f https://raw.githubusercontent.com/argoproj/argo-cd/stable/manifests/install.yaml

# 3.3 (Optional) Port-forward UI to localhost
kubectl -n argocd port-forward svc/argocd-server 8080:443

# 3.4 Get initial admin password
kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{{.data.password}}" | base64 -d; echo
# Login at https://localhost:8080 (user: admin)
```

---

## 4) Configure this repo before bootstrapping

Edit **two files** to point Argo CD at your repo:

1. `k8s/clusters/dev/root-application.yaml`
   - `spec.source.repoURL` → set to your git URL (e.g., `https://github.com/<org>/ielts-infra.git`)

2. `k8s/apps/dev/frontend.yaml`
   - `spec.source.repoURL` → same as above

Configure the **frontend** overlay:
- `k8s/apps/frontend/overlays/dev/kustomization.yaml`
  - Set `newName` to your image (e.g., `your-dockerhub-user/ielts-frontend`)
  - Set `newTag` to the tag you pushed (e.g., `dev-001`)

Configure the **Ingress host**:
- `k8s/apps/frontend/base/ingress.yaml`
  - Change `host: app.dev.example.com` to your domain.

> You can automate image tag bumps from your frontend CI by opening a PR that updates `newTag`.

---

## 5) Bootstrap Dev

```bash
# Apply the root application (Argo CD will create/sync everything under k8s/apps/dev/)
kubectl apply -f k8s/clusters/dev/root-application.yaml

# Watch Applications
kubectl -n argocd get applications.argoproj.io
# Or via UI (port-forwarded): https://localhost:8080
```

This will:
- Install **ingress-nginx**, **cert-manager**, **external-secrets**, **kube-prometheus-stack**
- Deploy your **frontend** app using the overlay settings

---

## 6) Point DNS and test

1. Get the Ingress Controller external IP:
   ```bash
   kubectl -n ingress-nginx get svc ingress-nginx-controller
   ```
2. Point your DNS `A` record (`app.dev.example.com`) to that IP.  
3. Once DNS resolves, test:
   ```bash
   curl -I http://app.dev.example.com/
   ```

> For quick/local testing, you can temporarily add to `/etc/hosts`:  
> `<INGRESS_IP>  app.dev.example.com`

---

## 7) HTTPS (Let’s Encrypt)

`cert-manager` is installed by Argo CD, but you need a **ClusterIssuer** and TLS config in your Ingress:

1. Apply the staging issuer (edit email first):
   ```bash
   kubectl apply -f k8s/apps/dev/addons/cert-manager/clusterissuer-staging.yaml
   ```

2. Add to your Ingress (or overlay patch):
   - Annotation:
     ```yaml
     cert-manager.io/cluster-issuer: letsencrypt-staging
     ```
   - TLS block:
     ```yaml
     tls:
       - hosts: ["app.dev.example.com"]
         secretName: ielts-frontend-tls
     ```

3. Wait for the cert:
   ```bash
   kubectl -n ielts-frontend get certificate
   kubectl -n ielts-frontend describe certificate ielts-frontend-tls
   ```

Switch to the **production** ACME endpoint when ready.

---

## 8) External Secrets (optional)

- Apply the sample store after setting your cloud credentials:
  ```bash
  kubectl apply -f k8s/apps/dev/addons/external-secrets/store-sample.yaml
  ```
- Then create an `ExternalSecret` in your app namespace to pull values into a K8s Secret.

> Example `ExternalSecret` (not included here) would reference the `ClusterSecretStore` name and keys from your cloud secret manager.

---

## 9) Monitoring (Prometheus/Grafana)

- The monitoring stack is installed via Helm.
- Default values: see `k8s/apps/dev/addons/values/monitoring-values.yaml`
- Get service details:
  ```bash
  kubectl -n monitoring get svc
  ```
- Adjust exposure (e.g., use `LoadBalancer` or Ingress) as needed.

---

## 10) CI / CD – image tag bumps

In your **frontend repo** CI:
1. Build & push an image tagged with commit SHA (e.g., `sha-abcdef`).
2. Open a PR to this repo that updates:
   ```yaml
   # k8s/apps/frontend/overlays/dev/kustomization.yaml
   images:
     - name: your-dockerhub-user/ielts-frontend
       newName: your-dockerhub-user/ielts-frontend
       newTag: sha-abcdef
   ```
3. Merge → Argo CD syncs automatically (auto‑sync is enabled).

This repo already includes `.github/workflows/lint-k8s.yml` to validate Kustomize output in PRs.

---

## 11) Add more apps/environments

- **Add an app:** copy `k8s/apps/frontend` → `k8s/apps/gateway` (or `platform/gateway`) and create a new Argo Application in `k8s/apps/dev/` pointing to the overlay path.
- **Add an environment:** create `k8s/clusters/staging/root-application.yaml` and `k8s/apps/frontend/overlays/staging`, then replicate the pattern.

---

## 12) Troubleshooting

- **Argo App stuck in OutOfSync / Missing resources**
  ```bash
  kubectl -n argocd describe application <name>
  kubectl -n argocd logs deploy/argocd-repo-server
  ```
- **Ingress 404 / 503**: check service/endpoints and labels selectors
  ```bash
  kubectl -n ielts-frontend get ingress,svc,endpoints
  kubectl -n ingress-nginx get svc ingress-nginx-controller
  ```
- **Pods CrashLoopBackOff**: inspect logs and events
  ```bash
  kubectl -n ielts-frontend logs deploy/ielts-frontend -c web
  kubectl -n ielts-frontend describe pod <pod-name>
  ```
- **No TLS cert**: check `cert-manager` logs and `CertificateRequest` events
  ```bash
  kubectl -n cert-manager logs deploy/cert-manager -f
  kubectl get certificaterequests.cert-manager.io -A
  ```

---

## 13) Security notes
- Do not commit real secrets. Use **External Secrets**, or SOPS-encrypted files.
- Lock down Argo CD admin after initial bootstrap (create users/SSO).
- Configure ResourceQuotas/LimitRanges per namespace for safety.

---

## 14) Summary
You now have:
- A centralized **infra repo** driving your cluster via **Argo CD**
- Add‑ons managed declaratively
- A repeatable pattern to add services/environments
- A CI guardrail for manifest validation

Happy shipping! 🚀
