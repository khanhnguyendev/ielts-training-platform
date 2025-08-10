"use client";

import { useHealth } from "@/features/health/useHealth";

export default function HomeClient() {
  const { data, isLoading, isError, refetch } = useHealth();

  return (
    <main style={{ padding: 24, fontFamily: "system-ui, sans-serif" }}>
      <h1>Next.js src/ skeleton</h1>
      <p>Health endpoints:</p>
      <ul>
        <li>
          <a href="/api/health">/api/health</a>
        </li>
        <li>
          <a href="/api/healthz">/api/healthz</a>
        </li>
      </ul>

      <h2 style={{ marginTop: 24 }}>React Query demo</h2>
      {isLoading && <p>Loading health…</p>}
      {isError && <p style={{ color: "red" }}>Failed to load health</p>}
      {data && (
        <pre
          style={{
            background: "#f6f8fa",
            padding: 12,
            borderRadius: 8,
            overflowX: "auto",
          }}
        >
{JSON.stringify(data, null, 2)}
        </pre>
      )}
      <button onClick={() => refetch()} style={{ marginTop: 12 }}>
        Refetch
      </button>
    </main>
  );
}


