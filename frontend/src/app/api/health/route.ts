import { NextResponse } from "next/server";

// Avoid static rendering/caching for health checks
export const dynamic = "force-dynamic";
export const runtime = "nodejs"; // ensure Node runtime (not edge)

export async function GET() {
  const payload = {
    status: "ok",
    uptime: process.uptime(),
    timestamp: new Date().toISOString(),
    version: process.env.APP_VERSION ?? "dev",
  } as const;

  return NextResponse.json(payload, { status: 200 });
}