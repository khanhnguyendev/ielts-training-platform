"use client";

import React, { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";

const MAX_FILE_BYTES = 10 * 1024 * 1024; // 10 MB

const fileSchema = z
  .instanceof(File, { message: "Audio file is required" })
  .refine((f) => f.type.startsWith("audio/"), { message: "Only audio files are allowed" })
  .refine((f) => f.size <= MAX_FILE_BYTES, { message: "Max size is 10MB" });

const formSchema = z.object({
  file: fileSchema,
});

type FormValues = z.infer<typeof formSchema>;

function pickSupportedMimeType(): string | undefined {
  // Try common containers across major browsers
  const candidates = [
    "audio/webm;codecs=opus",
    "audio/webm",
    "audio/mp4",
    "audio/mpeg",
  ];
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const MR: any = (globalThis as unknown as { MediaRecorder?: unknown }).MediaRecorder;
  if (!MR) return undefined;
  if (typeof MR.isTypeSupported !== "function") return undefined;
  for (const mime of candidates) {
    try {
      if (MR.isTypeSupported(mime)) return mime;
    } catch {
      // continue
    }
  }
  return undefined;
}

function extensionForMime(mime: string | undefined): string {
  if (!mime) return "webm"; // default
  if (mime.includes("webm")) return "webm";
  if (mime.includes("mpeg")) return "mp3";
  if (mime.includes("mp4")) return "m4a";
  return "webm";
}

export default function AudioRecorderForm() {
  const [isRecording, setIsRecording] = useState(false);
  const [, setRecordedBlob] = useState<Blob | null>(null);
  const [recordedUrl, setRecordedUrl] = useState<string | null>(null);
  const [uploadedUrl, setUploadedUrl] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const mediaRecorderRef = useRef<MediaRecorder | null>(null);
  const chunksRef = useRef<Blob[]>([]);
  const streamRef = useRef<MediaStream | null>(null);

  const supportedMime = useMemo(pickSupportedMimeType, []);
  const isMediaRecorderSupported = typeof window !== "undefined" && typeof (window as unknown as { MediaRecorder?: unknown }).MediaRecorder !== "undefined";

  const {
    // register, // not used because we set file via setValue
    handleSubmit,
    formState: { errors, isSubmitting },
    setValue,
    watch,
    reset,
  } = useForm<FormValues>({ resolver: zodResolver(formSchema) });

  const selectedFile = watch("file");

  useEffect(() => {
    return () => {
      // cleanup object URL
      if (recordedUrl) URL.revokeObjectURL(recordedUrl);
      // stop media tracks if any
      streamRef.current?.getTracks().forEach((t) => t.stop());
    };
  }, [recordedUrl]);

  const startRecording = useCallback(async () => {
    setError(null);
    setUploadedUrl(null);
    try {
      if (!isMediaRecorderSupported) {
        setError("MediaRecorder is not supported in this browser.");
        return;
      }
      const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
      streamRef.current = stream;
      const mr = new MediaRecorder(stream, supportedMime ? { mimeType: supportedMime } : undefined);
      chunksRef.current = [];
      mr.ondataavailable = (e) => {
        if (e.data && e.data.size > 0) chunksRef.current.push(e.data);
      };
      mr.onstop = () => {
        const blob = new Blob(chunksRef.current, { type: supportedMime || "audio/webm" });
        setRecordedBlob(blob);
        const url = URL.createObjectURL(blob);
        setRecordedUrl(url);
        // Push into form as File so validation/upload works the same path
        const ext = extensionForMime(blob.type);
        const file = new File([blob], `recording-${Date.now()}.${ext}`, { type: blob.type });
        setValue("file", file, { shouldValidate: true });
        // stop all tracks to release mic
        stream.getTracks().forEach((t) => t.stop());
        streamRef.current = null;
      };
      mediaRecorderRef.current = mr;
      mr.start();
      setIsRecording(true);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to start recording");
    }
  }, [isMediaRecorderSupported, setValue, supportedMime]);

  const stopRecording = useCallback(() => {
    try {
      mediaRecorderRef.current?.stop();
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to stop recording");
    } finally {
      setIsRecording(false);
    }
  }, []);

  const onFileChange = useCallback(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      setError(null);
      setUploadedUrl(null);
      const f = e.target.files?.[0];
      if (f) {
        setValue("file", f, { shouldValidate: true });
        const url = URL.createObjectURL(f);
        if (recordedUrl) URL.revokeObjectURL(recordedUrl);
        setRecordedUrl(url);
        setRecordedBlob(f);
      }
    },
    [recordedUrl, setValue]
  );

  const onSubmit = useCallback(
    async (values: FormValues) => {
      setError(null);
      setUploadedUrl(null);
      try {
        const formData = new FormData();
        formData.append("file", values.file);
        const res = await fetch("/api/audio/upload", { method: "POST", body: formData });
        if (!res.ok) throw new Error(`Upload failed ${res.status}`);
        const data = (await res.json()) as { id: string; url: string };
        setUploadedUrl(data.url);
      } catch (err) {
        setError(err instanceof Error ? err.message : "Upload failed");
      }
    },
    []
  );

  const resetAll = useCallback(() => {
    setIsRecording(false);
    setRecordedBlob(null);
    if (recordedUrl) URL.revokeObjectURL(recordedUrl);
    setRecordedUrl(null);
    setUploadedUrl(null);
    setError(null);
    reset();
  }, [recordedUrl, reset]);

  return (
    <div style={{ padding: 16 }}>
      <h2>Audio Recording, Upload and Playback</h2>

      <div style={{ marginTop: 12, marginBottom: 12 }}>
        {isMediaRecorderSupported ? (
          <div style={{ display: "flex", gap: 8, alignItems: "center", flexWrap: "wrap" }}>
            <button onClick={startRecording} disabled={isRecording}>
              Start recording
            </button>
            <button onClick={stopRecording} disabled={!isRecording}>
              Stop
            </button>
            <span style={{ fontSize: 12, color: isRecording ? "#dc2626" : "#6b7280" }}>
              {isRecording ? "Recording…" : supportedMime ? `Ready (${supportedMime})` : "Ready"}
            </span>
          </div>
        ) : (
          <p style={{ color: "#b45309" }}>
            MediaRecorder is not supported. Use the file picker below to upload a recording.
          </p>
        )}
      </div>

      <form onSubmit={handleSubmit(onSubmit)}>
        <div style={{ display: "flex", flexDirection: "column", gap: 8, maxWidth: 560 }}>
          <label>
            <span>Select or capture audio</span>
            <input
              type="file"
              accept="audio/*"
              capture
              onChange={onFileChange}
            />
          </label>

          {errors.file && (
            <span style={{ color: "#dc2626", fontSize: 12 }}>{errors.file.message}</span>
          )}

          {(recordedUrl || selectedFile) && (
            <audio style={{ marginTop: 8 }} controls src={recordedUrl ?? undefined}>
              Your browser does not support the audio element.
            </audio>
          )}

          <div style={{ display: "flex", gap: 8, marginTop: 8 }}>
            <button type="submit" disabled={!watch("file") || isSubmitting}>
              {isSubmitting ? "Uploading…" : "Upload"}
            </button>
            <button type="button" onClick={resetAll} disabled={isRecording || isSubmitting}>
              Reset
            </button>
          </div>
        </div>
      </form>

      {uploadedUrl && (
        <div style={{ marginTop: 16 }}>
          <p>Uploaded. Server URL:</p>
          <a href={uploadedUrl}>{uploadedUrl}</a>
          <audio style={{ display: "block", marginTop: 8 }} controls src={uploadedUrl}>
            Your browser does not support the audio element.
          </audio>
        </div>
      )}

      {error && (
        <p style={{ color: "#dc2626", marginTop: 12 }}>{error}</p>
      )}
    </div>
  );
}


