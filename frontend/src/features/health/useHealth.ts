"use client";

import { useQuery } from "@tanstack/react-query";
import { gateway } from "@/lib/gateway";

export interface HealthResponse {
  status: string;
  uptime: number;
  timestamp: string;
  version: string;
}

export function useHealth() {
  return useQuery({
    queryKey: ["health"],
    queryFn: () => gateway.health.get(),
    // health can be relatively fresh; align with provider defaults or override if needed
  });
}


