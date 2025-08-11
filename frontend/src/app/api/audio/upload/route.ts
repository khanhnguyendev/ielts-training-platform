import { NextRequest, NextResponse } from "next/server";
import { mkdir, writeFile } from "node:fs/promises";
import path from "node:path";
import crypto from "node:crypto";

function extensionForMime(mime: string | undefined): string {
  if (!mime) return "webm";
  if (mime.includes("webm")) return "webm";
  if (mime.includes("mpeg")) return "mp3";
  if (mime.includes("mp4")) return "m4a";
  if (mime.includes("ogg")) return "ogg";
  if (mime.includes("wav")) return "wav";
  return "bin";
}

export async function POST(req: NextRequest) {
  const form = await req.formData();
  const file = form.get("file");
  if (!(file instanceof File)) {
    return NextResponse.json({ error: "file is required" }, { status: 400 });
  }

  const id = crypto.randomUUID();
  const ext = extensionForMime(file.type);
  const filename = `${id}.${ext}`;

  const buffer = Buffer.from(await file.arrayBuffer());
  const uploadsDir = path.join(process.cwd(), "tmp", "uploads");
  await mkdir(uploadsDir, { recursive: true });
  const filePath = path.join(uploadsDir, filename);
  await writeFile(filePath, buffer);

  // Return a route URL that will stream the file back
  const url = `/api/audio/${filename}`;
  return NextResponse.json({ id: filename, url });
}


