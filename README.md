<div align="center">

# ⚙️ RegEx Engine

### A Regular Expression Engine built from scratch in pure Java
### No `java.util.regex`. 

![Java](https://img.shields.io/badge/Java-11%2B-orange?style=flat-square&logo=openjdk)
![Status](https://img.shields.io/badge/Status-Active%20Development-blue?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)
![IDE](https://img.shields.io/badge/IDE-IntelliJ%20IDEA-purple?style=flat-square&logo=intellijidea)

</div>

---

## Overview

Most developers use regex libraries without ever thinking about what happens underneath.  
This project implements a regex engine **entirely from scratch** — every symbol, every rule, every edge case handled manually in Java.

It does two things:

| Feature | Description |
|---------|-------------|
| **Pattern Matching** | Given a regex pattern and a text string, determines whether the text satisfies the pattern |
| **Pattern Generation** | Given a regex pattern, generates a valid sample string that would match it |

> Built as a portfolio and deep-learning project. Currently in active development with 4 known bugs being fixed.

---

## Project Structure

```

    ── RegEx/
        ├── Briefing/
        │   ├── Bugs.txt                ← known bugs with root cause and fix plan
        │   ├── Explaination.txt        ← line-by-line explanation of how each file works
        │   ├── FailedAttempts.txt      ← approaches tried that did not work and why
        │   ├── FutureRoadmap.txt       ← planned features and development phases
        │   └── Testing.txt            ← test results for all 10 patterns with analysis
        └── main/
            ├── Regex.java              ← entry point — handles input, routes to matcher or generator
            ├── RegExGen.java           ← generates a valid string from a given regex pattern
            ├── RegExMatch.java         ← validates whether a text matches a given regex pattern
            └── RegExSym.java          ← symbol constants (RegexToken) + buildSpecialSet() utility
```

---

## Supported Syntax

| Symbol | Meaning | Example |
|--------|---------|---------|
| `*` | Zero or more of previous char | `a*b` → `b`, `ab`, `aaab` |
| `+` | One or more of previous char | `a+` → `a`, `aaa` |
| `?` | Zero or one of previous char | `a?b` → `b`, `ab` |
| `.` | Any single character | `a.b` → `axb`, `a3b` |
| `^` | Start anchor / negation inside `[]` | `^abc`, `[^aeiou]` |
| `$` | End anchor | `abc$` |
| `[...]` | Character class — match one char from set | `[a-z]`, `[abc]`, `[0-9]` |
| `[^...]` | Negated character class | `[^aeiou]` |
| `(...)` | Grouping with optional quantifier | `(ab)+`, `(cat\|dog)` |
| `{n}` / `{n,m}` | Exact or range repetition | `a{3}`, `a{2,5}` |
| `\|` | Alternation — OR | `cat\|dog` |
| `\d` | Any digit | `\d{3}` |
| `\w` | Any word character (letter / digit / `_`) | `\w+` |
| `\s` | Whitespace | `\s` |
| `-` | Range operator inside `[]` | `[a-z]`, `[0-9]` |

---

## Architecture

The engine is split into 4 focused, single-responsibility classes:

```
RegExSym  ──►  RegExGen
     │
     └──────►  RegExMatch
                    ▲
               Regex.java (main)
```

**`RegExSym`**  
The foundation. Defines all special regex characters as named constants inside `RegexToken` and provides `buildSpecialSet()` which returns a `HashSet<Character>` for O(1) symbol lookup used across the entire engine.

**`RegExGen`**  
The generator. Walks the pattern character by character. For each special symbol it hits, it applies the corresponding generation logic — randomized repetition, character class sampling, group handling, alternation. Returns a valid string for the given pattern.

**`RegExMatch`**  
The matcher. Same character-by-character walk, but instead of building output it validates. Returns `false` the moment any check fails. Returns `true` only after the entire pattern passes every condition.

**`Regex`**  
The entry point. Handles `Scanner` input, routes to the correct engine based on user choice, and prints the result.

---

## Getting Started

### Requirements

- Java 11 or higher
- IntelliJ IDEA (recommended) or any Java IDE

### Run

```bash
# Clone the repository
git clone https://github.com/iseeasta/RegExEngine.git

# Open in IntelliJ IDEA
# Navigate to:
# RegEx/main/Regex.java
# Run the main() method
```

### Example Session

```
Welcome to the Regex Engine!

Choose options from below:
1.) Pattern Matching
2.) Pattern Generation

Option: 2
Please enter the RegEx pattern: [a-z]{2,4}

Generated string: xkr
```

```
Option: 1
Please enter the RegEx pattern: a+b
Now enter the text to match against: aaab

Match result: true
```

---

## Known Bugs

> All bugs are documented in detail in `Briefing/Bugs.txt` with root cause analysis and fix plans.

| # | Bug | Affected Patterns | Status |
|---|-----|-------------------|--------|
| 1 | Double append — quantifiers re-add char already added by literal branch | `a?b?c`, `a+b+` | 🔧 In Progress |
| 2 | `]` leaking into output — `[` case not handling its own quantifier | `[abc]+`, `[a-z]{3}` | 🔧 In Progress |
| 3 | `{` index math breaks for multi-char tokens like `\d` or `[...]` | `\d{2,4}`, `[a-z]{3}` | 🔧 In Progress |
| 4 | `\|` inside `()` not processed as OR — repeated as a literal string | `(ab\|cd)+` | 🔧 In Progress |

---

## Roadmap

### Phase 1 — Fix All Bugs *(current)*
Resolve all 4 identified core bugs. Root cause and fix strategy documented in `Briefing/Bugs.txt`.

### Phase 2 — JavaFX Desktop UI
Replace the terminal interface with a proper desktop application:
- Pattern input with live generated output
- Color-coded match results — green for match, red for fail
- Clickable regex reference sidebar that auto-inserts symbols
- Pattern test history panel
- Dark mode by default

### Phase 3 — Log File Error Detection
Real-world utility feature:
- Load any `.log` file into the engine
- Define a regex for what a valid or error line looks like
- Engine scans line by line, flags matches and violations
- Outputs a summary report with line numbers, error counts, and `.txt` export

### Phase 4 — Open / Ongoing
Planned ideas: regex explainer (pattern → plain English description), pattern suggester, batch testing mode, named capture groups, step-by-step match visualizer.

---

## What I Learned Building This

- How regex engines tokenize and walk patterns internally
- Why `HashSet` is chosen over arrays for symbol lookup — O(1) vs O(n)
- Java Streams — `.chars().filter().count()` for character frequency analysis
- Why lambda variables must be `effectively final` in Java and how to work around it
- The `Scanner.nextLine()` bug that silently breaks input after `nextInt()`
- Recursive pattern splitting for alternation (`|`) handling
- The architectural difference between generating for logic vs generating for output

---

## Tech Stack

| | |
|---|---|
| Language | Java 11+ |
| IDE | IntelliJ IDEA |
| Build | Manual — no Maven/Gradle yet |
| UI *(planned)* | JavaFX with FXML + CSS |

---

## License

This project is licensed under the **MIT License** — free to use, modify, and distribute.  
See [`LICENSE`](./LICENSE) for full details.

---

[![GitHub](https://img.shields.io/badge/GitHub-iseeasta-black?style=flat-square&logo=github)](https://github.com/iseeasta)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-iseeasta-blue?style=flat-square&logo=linkedin)](https://linkedin.com/in/iseeasta)
