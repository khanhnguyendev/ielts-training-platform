import { jsonFetch } from "@/lib/fetcher";

// Thin adapter around Gateway API. Replace implementation with OpenAPI SDK when available.
// Centralizing here means hooks/components don't need changes later.

export const gateway = {
  health: {
    get: () => jsonFetch<{
      status: string;
      uptime: number;
      timestamp: string;
      version: string;
    }>("/api/health"),
  },
};


