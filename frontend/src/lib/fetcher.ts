export type HttpMethod = "GET" | "POST" | "PUT" | "PATCH" | "DELETE";

export type JsonRequestOptions<TBody> = Omit<
  RequestInit,
  "body" | "method" | "headers"
> & {
  method?: HttpMethod;
  body?: TBody;
  headers?: HeadersInit;
};

export async function jsonFetch<TResponse, TBody = unknown>(
  url: string,
  options: JsonRequestOptions<TBody> = {}
): Promise<TResponse> {
  const { method = "GET", body, headers, ...rest } = options;

  const res = await fetch(url, {
    method,
    headers: {
      "Content-Type": "application/json",
      ...(headers || {}),
    },
    body: body != null ? JSON.stringify(body) : undefined,
    ...rest,
  });

  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(`Request failed ${res.status}: ${text}`);
  }

  if (res.status === 204) {
    return undefined as unknown as TResponse;
  }

  return (await res.json()) as TResponse;
}


