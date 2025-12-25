# bitwarden-deduplicator

A small script using the typelevel toolkit to remove duplicates from Bitwarden's CSV password export file.

## Usage

To run this script, use Scala CLI and pass the file location as an argument:

```bash
scala-cli run . -- /path/to/your/bitwarden_export.csv
```

This will process your Bitwarden CSV export file and remove any duplicate entries.
