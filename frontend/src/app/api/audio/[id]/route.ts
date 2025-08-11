// Using standard Web Response for binary payload
import { readFile, stat } from "node:fs/promises";
import path from "node:path";

function mimeForFilename(name: string): string {
  if (name.endsWith(".webm")) return "audio/webm";
  if (name.endsWith(".mp3")) return "audio/mpeg";
  if (name.endsWith(".m4a")) return "audio/mp4";
  if (name.endsWith(".ogg")) return "audio/ogg";
  if (name.endsWith(".wav")) return "audio/wav";
  return "application/octet-stream";
}

export async function GET(_req: Request, context: unknown) {
  const { id } = (context as { params: { id: string } }).params;
  // Prevent path traversal
  if (id.includes("/") || id.includes("..")) {
    return new Response("Bad Request", { status: 400 });
  }
  const uploadsDir = path.join(process.cwd(), "tmp", "uploads");
  const filePath = path.join(uploadsDir, id);
  try {
    const s = await stat(filePath);
    if (!s.isFile()) throw new Error("not a file");
    const data = await readFile(filePath);
    const body = new Uint8Array(data);
    return new Response(body, {
      status: 200,
      headers: {
        "Content-Type": mimeForFilename(id),
        "Content-Length": String(s.size),
        "Cache-Control": "public, max-age=31536000, immutable",
      },
    });
  } catch {
    return new Response("Not Found", { status: 404 });
  }
}


