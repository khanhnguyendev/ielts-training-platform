"use client";

import { useState } from "react";

// Mock data for IELTS scores
const mockScores = {
  listening: 7.5,
  reading: 8.0,
  writing: 6.5,
  speaking: 7.0,
};

// Calculate overall score (average of all bands)
const calculateOverallScore = (scores: typeof mockScores) => {
  const total = scores.listening + scores.reading + scores.writing + scores.speaking;
  return Math.round(total / 4 * 2) / 2; // Round to nearest 0.5
};

const getBandColor = (score: number) => {
  if (score >= 8.0) return "bg-green-500";
  if (score >= 7.0) return "bg-blue-500";
  if (score >= 6.0) return "bg-yellow-500";
  if (score >= 5.0) return "bg-orange-500";
  return "bg-red-500";
};

const getBandLabel = (score: number) => {
  if (score >= 8.0) return "Excellent";
  if (score >= 7.0) return "Good";
  if (score >= 6.0) return "Competent";
  if (score >= 5.0) return "Modest";
  return "Limited";
};

export default function ScoreClient() {
  const [scores] = useState(mockScores);
  const overallScore = calculateOverallScore(scores);

  const bands = [
    { name: "Listening", score: scores.listening, icon: "🎧" },
    { name: "Reading", score: scores.reading, icon: "📖" },
    { name: "Writing", score: scores.writing, icon: "✍️" },
    { name: "Speaking", score: scores.speaking, icon: "🗣️" },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-4">
      <div className="max-w-4xl mx-auto">
        {/* Header */}
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-gray-800 mb-2">IELTS Score Report</h1>
          <p className="text-gray-600">Your comprehensive band scores and overall performance</p>
        </div>

        {/* Overall Score Card */}
        <div className="bg-white rounded-2xl shadow-lg p-8 mb-8">
          <div className="text-center">
            <h2 className="text-2xl font-semibold text-gray-700 mb-4">Overall Band Score</h2>
            <div className="inline-flex items-center justify-center w-32 h-32 rounded-full bg-gradient-to-r from-blue-500 to-purple-600 text-white">
              <div className="text-center">
                <div className="text-4xl font-bold">{overallScore}</div>
                <div className="text-sm opacity-90">{getBandLabel(overallScore)}</div>
              </div>
            </div>
            <p className="text-gray-600 mt-4">
              Average of all four skills: {((scores.listening + scores.reading + scores.writing + scores.speaking) / 4).toFixed(2)}
            </p>
          </div>
        </div>

        {/* Individual Band Scores */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {bands.map((band) => (
            <div key={band.name} className="bg-white rounded-xl shadow-md p-6 hover:shadow-lg transition-shadow">
              <div className="text-center">
                <div className="text-3xl mb-3">{band.icon}</div>
                <h3 className="text-lg font-semibold text-gray-800 mb-2">{band.name}</h3>
                <div className={`inline-flex items-center justify-center w-16 h-16 rounded-full ${getBandColor(band.score)} text-white mb-3`}>
                  <span className="text-xl font-bold">{band.score}</span>
                </div>
                <div className="text-sm text-gray-600">{getBandLabel(band.score)}</div>
                
                {/* Score breakdown */}
                <div className="mt-4 text-xs text-gray-500">
                  <div className="flex justify-between">
                    <span>Band Level:</span>
                    <span className="font-medium">{Math.floor(band.score)}</span>
                  </div>
                  <div className="flex justify-between">
                    <span>Decimal:</span>
                    <span className="font-medium">{(band.score % 1).toFixed(1)}</span>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>

        {/* Score Summary */}
        <div className="bg-white rounded-xl shadow-md p-6 mt-8">
          <h3 className="text-xl font-semibold text-gray-800 mb-4">Score Summary</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <h4 className="font-medium text-gray-700 mb-2">Highest Score</h4>
              <p className="text-lg font-semibold text-green-600">
                Reading: {Math.max(...Object.values(scores))}
              </p>
            </div>
            <div>
              <h4 className="font-medium text-gray-700 mb-2">Lowest Score</h4>
              <p className="text-lg font-semibold text-red-600">
                Writing: {Math.min(...Object.values(scores))}
              </p>
            </div>
            <div>
              <h4 className="font-medium text-gray-700 mb-2">Score Range</h4>
              <p className="text-lg font-semibold text-blue-600">
                {Math.min(...Object.values(scores))} - {Math.max(...Object.values(scores))}
              </p>
            </div>
            <div>
              <h4 className="font-medium text-gray-700 mb-2">Overall Band</h4>
              <p className="text-lg font-semibold text-purple-600">
                {overallScore} ({getBandLabel(overallScore)})
              </p>
            </div>
          </div>
        </div>

        {/* Footer */}
        <div className="text-center mt-8 text-gray-500 text-sm">
          <p>This is a mock score report for demonstration purposes</p>
          <p className="mt-1">Generated on {new Date().toLocaleDateString()}</p>
        </div>
      </div>
    </div>
  );
}
