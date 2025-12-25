# bitwarden-deduplicator

A small script using the typelevel toolkit to remove duplicates from the CSV password export file of Bitwarden.

## Usage

To run this script, use Scala CLI and pass the file location as an argument:

```bash
scala-cli run . -- /path/to/your/bitwarden_export.csv
```

This will process your Bitwarden CSV export file and remove any duplicate entries, helping you maintain a clean password database.
