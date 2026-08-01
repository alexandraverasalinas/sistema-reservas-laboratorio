const fs = require("fs");
const path = require("path");

const replacements = [
  ["\u00C3\u00A1", "\u00E1"],
  ["\u00C3\u00A9", "\u00E9"],
  ["\u00C3\u00AD", "\u00ED"],
  ["\u00C3\u00B3", "\u00F3"],
  ["\u00C3\u00BA", "\u00FA"],
  ["\u00C3\u00B1", "\u00F1"],

  ["\u00C3\u0081", "\u00C1"],
  ["\u00C3\u0089", "\u00C9"],
  ["\u00C3\u008D", "\u00CD"],
  ["\u00C3\u0093", "\u00D3"],
  ["\u00C3\u009A", "\u00DA"],
  ["\u00C3\u0091", "\u00D1"],

  ["\u00C2\u00BF", "\u00BF"],
  ["\u00C2\u00A1", "\u00A1"],
  ["\u00C2\u00B0", "\u00B0"],
  ["\u00C2", ""],

  ["\u00C3\u0192\u00C2\u00A1", "\u00E1"],
  ["\u00C3\u0192\u00C2\u00A9", "\u00E9"],
  ["\u00C3\u0192\u00C2\u00AD", "\u00ED"],
  ["\u00C3\u0192\u00C2\u00B3", "\u00F3"],
  ["\u00C3\u0192\u00C2\u00BA", "\u00FA"],
  ["\u00C3\u0192\u00C2\u00B1", "\u00F1"],

  ["\u00E2\u20AC\u201C", "\u2013"],
  ["\u00E2\u20AC\u201D", "\u2014"],
  ["\u00E2\u20AC\u02DC", "\u2018"],
  ["\u00E2\u20AC\u2122", "\u2019"],
  ["\u00E2\u20AC\u0153", "\u201C"],
  ["\u00E2\u20AC\u009D", "\u201D"]
];

const extensions = [".html", ".ts", ".css"];

function walk(dir, files = []) {
  if (!fs.existsSync(dir)) return files;

  for (const item of fs.readdirSync(dir)) {
    const fullPath = path.join(dir, item);
    const stat = fs.statSync(fullPath);

    if (stat.isDirectory()) {
      walk(fullPath, files);
    } else if (extensions.includes(path.extname(fullPath))) {
      files.push(fullPath);
    }
  }

  return files;
}

const files = walk(path.join(__dirname, "src"));
let changed = 0;
let pending = [];

for (const file of files) {
  let text = fs.readFileSync(file, "utf8");
  const original = text;

  let modified = true;

  while (modified) {
    modified = false;

    for (const [bad, good] of replacements) {
      if (text.includes(bad)) {
        text = text.split(bad).join(good);
        modified = true;
      }
    }
  }

  if (text !== original) {
    fs.writeFileSync(file, text, "utf8");
    console.log("Corregido:", file);
    changed++;
  }

  if (text.includes("Ã") || text.includes("Â")) {
    pending.push(file);
  }
}

console.log("");
console.log("Archivos corregidos:", changed);

if (pending.length > 0) {
  console.log("");
  console.log("Aún quedan archivos con posibles caracteres raros:");
  pending.forEach(file => console.log(" - " + file));
} else {
  console.log("");
  console.log("No quedan caracteres raros tipo Ã o Â.");
}
