package com.ats;

import com.ats.extractor.KeywordExtractor;
import com.ats.input.JDLoader;
import com.ats.input.ResumeLoader;
import com.ats.matcher.MatchResult;
import com.ats.matcher.SkillMatcher;
import com.ats.parser.TextCleaner;
import com.ats.scorer.MatchScorer;
import com.ats.suggestion.SuggestionEngine;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Smart Resume Analyzer - ATS Simulation Tool
 * Analyzes resume-job description alignment and provides actionable feedback.
 */
public class Main {

    private static final String DEFAULT_RESUME = "sample_data/sample_resume.txt";
    private static final String DEFAULT_JD = "sample_data/sample_jd.txt";

    public static void main(String[] args) {
        try {
            // Determine file paths
            String resumePath = args.length > 0 ? args[0] : DEFAULT_RESUME;
            String jdPath = args.length > 1 ? args[1] : DEFAULT_JD;

            System.out.println("Smart Resume Analyzer - Starting Analysis...\n");

            // 1. Load files
            String resumeText = ResumeLoader.loadResume(resumePath);
            String jdText = JDLoader.loadJD(jdPath);

            // 2. Clean text
            String cleanedResume = TextCleaner.clean(resumeText);
            String cleanedJD = TextCleaner.clean(jdText);

            // 3. Extract skills
            Set<String> resumeSkills = KeywordExtractor.extractSkills(cleanedResume);
            Set<String> jdSkills = KeywordExtractor.extractSkills(cleanedJD);

            // 4. Match skills
            MatchResult matchResult = SkillMatcher.matchSkills(resumeSkills, jdSkills);

            // 5. Calculate score
            double score = MatchScorer.calculateScore(
                    matchResult.getMatchedSkills(),
                    jdSkills);

            // 6. Generate suggestions
            List<String> suggestions = SuggestionEngine.generateSuggestions(
                    matchResult.getMissingSkills(),
                    matchResult.getExtraSkills(),
                    score);

            // 7. Display results
            printResults(score, matchResult, suggestions);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("\nUsage: java -jar smart-resume-analyzer.jar [resume_file] [jd_file]");
            System.err.println("If no files specified, uses default sample files.");
            System.exit(1);
        }
    }

    /**
     * Print formatted results to console
     * Isolated method for easy refactoring to ResultPrinter class later
     */
    private static void printResults(double score, MatchResult matchResult, List<String> suggestions) {
        System.out.println("========================================");
        System.out.printf("ATS MATCH SCORE: %.0f%%\n", score);
        System.out.println("========================================\n");

        // Matched skills (sorted alphabetically)
        Set<String> matched = new TreeSet<>(matchResult.getMatchedSkills());
        System.out.println("Matched Skills (" + matched.size() + "):");
        if (matched.isEmpty()) {
            System.out.println("  (none)");
        } else {
            matched.forEach(skill -> System.out.println("  - " + skill));
        }
        System.out.println();

        // Missing skills (sorted alphabetically)
        Set<String> missing = new TreeSet<>(matchResult.getMissingSkills());
        System.out.println("Missing Skills (" + missing.size() + "):");
        if (missing.isEmpty()) {
            System.out.println("  (none)");
        } else {
            missing.forEach(skill -> System.out.println("  - " + skill));
        }
        System.out.println();

        // Extra skills (sorted alphabetically)
        Set<String> extra = new TreeSet<>(matchResult.getExtraSkills());
        System.out.println("Extra Skills (" + extra.size() + "):");
        if (extra.isEmpty()) {
            System.out.println("  (none)");
        } else {
            extra.forEach(skill -> System.out.println("  - " + skill));
        }
        System.out.println();

        // Suggestions
        System.out.println("Suggestions:");
        suggestions.forEach(suggestion -> System.out.println("  • " + suggestion));
        System.out.println("========================================");
    }
}
