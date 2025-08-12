# IELTS Frontend Kubernetes Manifests

This directory contains the Kubernetes manifests for deploying the IELTS Frontend application with comprehensive health monitoring and auto-scaling capabilities.

## Components

### 1. Deployment (`deployment.yaml`)
- **Replicas**: 2 (minimum for high availability)
- **Strategy**: RollingUpdate with zero downtime
- **Health Probes**:
  - **Readiness Probe**: `/api/health` - Determines if pod is ready to receive traffic
  - **Liveness Probe**: `/api/healthz` - Determines if pod is alive and should be restarted
  - **Startup Probe**: `/api/health` - Prevents liveness probe from interfering during startup

### 2. Service (`service.yaml`)
- **Type**: ClusterIP (internal access)
- **Port**: 80 → 3000 (container port)
- **Prometheus Integration**: Annotations for metrics scraping

### 3. HorizontalPodAutoscaler (`hpa.yaml`)
- **Scaling**: Based on CPU (70%) and Memory (80%) utilization
- **Range**: 2-10 replicas
- **Behavior**: Conservative scale-down, aggressive scale-up

### 4. Ingress (`ingress.yaml`)
- **External Access**: Routes traffic from outside the cluster
- **SSL/TLS**: Configured for secure communication

## Health Probe Configuration

### Readiness Probe
```yaml
readinessProbe:
  httpGet:
    path: /api/health
    port: 3000
  initialDelaySeconds: 10
  periodSeconds: 5
  timeoutSeconds: 3
  failureThreshold: 3
```
- **Purpose**: Ensures pod is ready to serve traffic
- **Endpoint**: Returns detailed health status with uptime and version
- **Timing**: Checks every 5 seconds after 10-second initial delay

### Liveness Probe
```yaml
livenessProbe:
  httpGet:
    path: /api/healthz
    port: 3000
  initialDelaySeconds: 30
  periodSeconds: 10
  timeoutSeconds: 5
  failureThreshold: 3
```
- **Purpose**: Detects if application is stuck or deadlocked
- **Endpoint**: Simple "ok" response for quick checks
- **Timing**: Checks every 10 seconds after 30-second initial delay

### Startup Probe
```yaml
startupProbe:
  httpGet:
    path: /api/health
    port: 3000
  initialDelaySeconds: 5
  periodSeconds: 5
  failureThreshold: 30
```
- **Purpose**: Prevents liveness probe from killing slow-starting containers
- **Timing**: Allows up to 2.5 minutes (30 × 5s) for startup

## Security Features

- **Non-root execution**: Runs as user 1000
- **Read-only filesystem**: Prevents file system modifications
- **Dropped capabilities**: Removes all Linux capabilities
- **No privilege escalation**: Prevents gaining additional privileges

## Resource Management

- **Requests**: 128Mi memory, 100m CPU (guaranteed resources)
- **Limits**: 512Mi memory, 500m CPU (maximum resources)

## Monitoring Integration

The service includes Prometheus annotations for automatic metrics scraping:
- `prometheus.io/scrape: "true"`
- `prometheus.io/port: "3000"`
- `prometheus.io/path: "/api/health"`

## Usage

Deploy using Kustomize:
```bash
kubectl apply -k infra/k8s/apps/frontend/base/
```

Or apply individual manifests:
```bash
kubectl apply -f infra/k8s/apps/frontend/base/
```
