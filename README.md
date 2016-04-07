# Auto-Tutorial Demonstration #
This repository contains some files as a demonstration of how annotated source files can be converted to readable markdown/pdf files. (Please forgive the bad python programming).

## Motivation ##
To provide an easy way to produce a blog-like document from a source file. For example, [this](http://pacman.blog.br/blog/2012/12/29/spell-correct-in-gawk/), in my opinion, is much easier to read, follow, and understand than an annotated source file. I wanted to automate this process so that a source file can be annotated as you go along, and then it can be converted to a beautiful-looking document at the end.

If certain parts of the conversion are awkward, e.g. indentation of braces on the last line, then you can always just modify the markdown file directly after conversion.

## Explanation of the scripts ##

### `auto-tutorial` ###
This script converts an annotated source file to a formatted markdown file. This is the most important script in the repository. 

**Usage**: `auto-tutorial [file] [language]`

`[language]` is the tag that would be given if highlighting text on GitHub, e.g. `java`, `cpp`, etc.

### `convert-markdown` ###
Converts a markdown file to pdf. This requires the most recent version of [Pandoc](https://github.com/jgm/pandoc/releases) (and possibly some other LaTeX dependencies).

**Usage**: `convert-markdown [file]`

### `join-tuts` ###
This is mostly a convenience script. It takes a list of source files, converts them to markdown, and joins them together.

**Usage**: `join-tuts [language] [file...]`

### `Makefile`/`prettify` ###
These are convenience scripts which need to be modified. Simply running `make` or `./prettify` will convert all specified files to markdown and then generate a pdf.

## Workflow ##
Place the scripts in the root of your workspace. To produce markdown versions of your annotated source files, modify the Makefile/Bash script appropriately and then run it.

Alternatively, you can place the scripts in `/usr/local/bin` so that they can be run from anywhere (then modify the Makefile/Bash script by removing the `./` prefix).

## How to Annotate Source Files ##
| Tag | Meaning |
| --- | ------- |
| `#start-page#`  | indicates the start of a markdown page |
| `#end-page#`    | indicates the end of a markdown page |
| `#start-text#`  | indicates the start of a text block |
| `#end-text#`    | indicates the end of a text block |
| `#start-ignore#`| indicates the start of an ignored section |
| `#end-ignore#`  | indicates the end of an ignored section |

- The page-start defaults to the first line of the code, and page-end defaults to the last line. Anything before the page-start is ignored, and anything after the page-end is ignored.
- Standard markdown can be used within text blocks, e.g. `# Heading` indicates a heading, `**Word**` indicates an emphasised word etc.
- Anything between `#start-ignore#` and `#end-ignore#` is ignored.

Please see the example files.
