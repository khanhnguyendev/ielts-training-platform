export default function Home() {
  return (
    <main style={{padding: 24, fontFamily: "system-ui, sans-serif"}}>
      <h1>Next.js src/ skeleton</h1>
      <p>Health endpoints:</p>
      <ul>
        <li><a href="/api/health">/api/health</a></li>
        <li><a href="/api/healthz">/api/healthz</a></li>
      </ul>
    </main>
  );
}
