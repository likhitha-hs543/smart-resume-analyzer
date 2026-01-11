# Smart Resume Analyzer

A rule-based ATS (Applicant Tracking System) simulation tool that analyzes resume-job description alignment and provides actionable feedback.

## Project Overview

This tool helps job seekers understand how well their resume matches a job description by:
- Calculating an ATS-style match percentage
- Identifying matched, missing, and extra skills
- Generating plain-English improvement suggestions

**This is a rule-based system**, not AI. It uses keyword matching and set operations to provide clear, explainable results.

## Features

- **Resume Parsing**: Supports both PDF and plain text (.txt) formats
- **Skill Extraction**: Uses a curated whitelist of 75+ technical skills
- **Match Analysis**: Set-based comparison (matched, missing, extra skills)
- **ATS Scoring**: Percentage-based score reflecting JD coverage
- **Actionable Suggestions**: Rule-based feedback for resume improvement
- **Console Output**: Clean, formatted results with skill counts

## Tech Stack

- **Language**: Java 17
- **Build Tool**: Maven (with wrapper included)
- **PDF Parsing**: Apache PDFBox 2.0.30
- **Execution**: Standalone executable JAR

## How It Works

```
Resume Text + Job Description
        ↓
  Text Cleaning (lowercase, remove punctuation/numbers)
        ↓
  Skill Extraction (whitelist filtering + stop words)
        ↓
  Skill Matching (set intersection & difference)
        ↓
  Score Calculation (matched/total JD skills × 100)
        ↓
  Suggestion Generation (rule-based feedback)
        ↓
  Formatted Console Output
```

### Module Breakdown

| Module | Responsibility |
|--------|---------------|
| `ResumeLoader` | Extract raw text from PDF/TXT resume files |
| `JDLoader` | Load job description from text file |
| `TextCleaner` | Normalize text (lowercase, remove punctuation/numbers) |
| `KeywordExtractor` | Extract skills using whitelist + stop word filtering |
| `SkillMatcher` | Compare resume vs JD skills (matched, missing, extra) |
| `MatchScorer` | Calculate ATS match percentage |
| `SuggestionEngine` | Generate improvement suggestions |
| `Main` | Orchestrate pipeline and format output |

## Design Decisions

### Why Rule-Based (Not ML)?
- **Simplicity**: No training data or model complexity
- **Explainability**: Every match is traceable
- **Speed**: Instant analysis, no server required
- **Clarity**: Perfect for understanding ATS basics

### Why Console (Not GUI)?
- **Focus**: Prioritizes algorithm correctness over presentation
- **Simplicity**: One-day build constraint
- **Portability**: Works anywhere Java runs

### Why Skill Whitelist?
- **Precision**: Differentiates technical skills ("java") from generic words ("experience")
- **Quality**: Dramatically improves signal-to-noise ratio
- **Transparency**: Skills list is visible and editable

### Why No Database?
- **MVP Scope**: In-memory processing is sufficient
- **Simplicity**: No setup or configuration required
- **Portability**: Single JAR file, no dependencies

## Sample Output

```
========================================
ATS MATCH SCORE: 43%
========================================

Matched Skills (3):
  - git
  - java
  - sql

Missing Skills (4):
  - aws
  - docker
  - spring
  - springboot

Extra Skills (3):
  - android
  - firebase
  - python

Suggestions:
  • Consider adding experience with: aws, docker, spring, springboot
  • align your additional skills (3 found) with the job requirements
  • Resume alignment needs significant improvement - focus on matching key job requirements
========================================
```

## How to Run

### Option 1: Using the JAR (Recommended)
```bash
# Build the project
.\mvnw.cmd clean package

# Run with default sample files
java -jar target/smart-resume-analyzer-1.0.0.jar

# Run with custom files
java -jar target/smart-resume-analyzer-1.0.0.jar path/to/resume.pdf path/to/job_description.txt
```

### Option 2: Using Maven Wrapper
```bash
# Quick test with sample data
.\mvnw.cmd exec:java -Dexec.mainClass="com.ats.Main"
```

### Requirements
- Java 17 or higher (built and tested with Java 21, targeting Java 17 bytecode)
- No other dependencies (Maven wrapper included)

## Project Structure

```
smart-resume-analyzer/
├── src/main/java/com/ats/
│   ├── input/
│   │   ├── ResumeLoader.java
│   │   └── JDLoader.java
│   ├── parser/
│   │   └── TextCleaner.java
│   ├── extractor/
│   │   └── KeywordExtractor.java
│   ├── matcher/
│   │   ├── MatchResult.java
│   │   └── SkillMatcher.java
│   ├── scorer/
│   │   └── MatchScorer.java
│   ├── suggestion/
│   │   └── SuggestionEngine.java
│   └── Main.java
├── src/main/resources/
│   └── skills.txt (75+ technical skills)
├── sample_data/
│   ├── sample_resume.txt
│   └── sample_jd.txt
├── pom.xml
├── mvnw.cmd (Maven wrapper)
└── README.md
```

## Limitations

**Text Processing**:
- Number removal affects version numbers: `Java 17` → `java`, `HTML5` → `html`
- Punctuation removal has edge cases: `node.js` → `nodejs`, `c++` → `c`

**Matching Logic**:
- No synonym handling: "js" vs "javascript", "springboot" vs "spring boot" treated as different
- No semantic understanding: Only exact keyword matches
- English-only: No multi-language support

**Scoring**:
- Simple ratio formula: `matched_skills / total_jd_skills × 100`
- Does not weight skills by importance
- Extra skills don't reduce score (design choice)

**Input Constraints**:
- One resume + one JD per run (no batch processing)
- PDF text extraction quality depends on PDF structure

These are **intentional MVP tradeoffs** documented for transparency. See Future Enhancements for planned improvements.

## Future Enhancements

- **Synonym Mapping**: Handle "js"/"javascript", "ML"/"machine learning"
- **Weighted Scoring**: Assign importance levels to different skills
- **ML Integration**: Use embeddings for semantic skill matching
- **Multi-Language Support**: Extend beyond English
- **REST API**: Web service interface
- **Batch Processing**: Analyze multiple resumes at once
- **Confidence Scores**: Add match confidence levels
- **PDF Improvements**: Better handling of complex PDF layouts
- **Web UI**: Interactive dashboard with charts

## License

MIT License - feel free to use and modify for learning purposes.

## Author

Built as a portfolio project demonstrating:
- Clean code architecture
- Single responsibility principle
- Honest documentation of tradeoffs
- Production-ready build tooling

---

**Portfolio Note**: This is a rule-based MVP, not an AI-powered tool. The focus is on understandable, explainable logic that demonstrates software engineering fundamentals: clear separation of concerns, defensive programming (immutable sets), and honest tradeoff documentation.
