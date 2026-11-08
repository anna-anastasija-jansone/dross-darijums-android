# Drošs Darījums — sprinta vadītājas praktiskā lapa

## Sprinta mērķis

Līdz sprinta beigām izstrādāt un dzīvajā demonstrēt vienu pilnu vertikālo šķēli: lietotājs Android lietotnē izveido aizsargātu darījumu, ievade tiek pārbaudīta, maksājums tiek simulēts un jaunais darījums parādās sarakstā ar statusu.

## Piedāvātais sprinta backlog

| ID | Backlog vienība | Akcepta kritēriji | Novērtējums |
|---|---|---|---:|
| PB-01 | Projekta karkass un navigācija | Projekts atveras Android Studio; var pāriet no saraksta uz darījuma izveidi un atpakaļ; CI izpilda vienību testus. | 3 h |
| PB-02 | Darījuma formas ievade | Var ievadīt preci, pārdevēju un summu; komats un punkts tiek pieņemts kā decimālatdalītājs. | 3 h |
| PB-03 | Ievades validācija | Tukši lauki, nederīga summa, summa ārpus 1–5000 € un vairāk nekā divas decimālzīmes parāda konkrētu kļūdu. | 2 h |
| PB-04 | Darījuma pārskats | Pirms apstiprināšanas redzama prece, pārdevējs, summa un maksājuma paskaidrojums; iespējams atgriezties labot. | 2 h |
| PB-05 | Maksājuma simulācija un stāvokļi | Redzams ielādes stāvoklis; simulācija beidzas ar veiksmi vai skaidru kļūdu; kļūdas gadījumā var mēģināt atkārtoti. | 3 h |
| PB-06 | Darījumu saraksts | Pēc veiksmes darījums redzams sarakstā ar summu, datumu un statusu; ir tukšais un gatavais stāvoklis. | 3 h |
| PB-07 | Testi un piekļūstamība | Kritiskajai validācijai ir vienību testi; galvenajām darbībām zona ir vismaz 44 × 44 dp; galvenais teksts sasniedz 4,5:1 kontrastu. | 2 h |

Sapulcē novērtējumi ir jāpārskata. Ja Annai sprintā nav aptuveni 18 stundu, vienības jāizņem, nevis jāsamazina to kvalitāte.

## 20 minūšu plānošanas sapulce

### 0:00–3:00 — mērķis un pašreizējais stāvoklis

1. Pasaki sprinta mērķi vienā teikumā.
2. Atver K2 prototipu un īsi izpildi galveno plūsmu.
3. Atver repozitoriju un uzdevumu dēli; parādi, kas jau sagatavots.

### 3:00–9:00 — jautājumi un akcepta kritēriji

Ejiet cauri PB-01–PB-07. Komandas biedri jautā, ko nozīmē “pabeigts”. Neskaidru kritēriju pārraksti uz vietas tā, lai to var pārbaudīt ar konkrētu darbību.

### 9:00–14:00 — novērtēšana un apjoms

Pasaki, cik stundas reāli vari strādāt. Novērtējiet katru vienību, saskaitiet stundas un izņemiet zemākas prioritātes vienības, ja apjoms pārsniedz pieejamo laiku. Kartītē ieraksti tikai izvēlēto apjomu.

### 14:00–18:00 — uzdevumu sadale

- Anna: izvēlētā funkcionalitāte un demonstrējams rezultāts.
- Komandas biedrs A: pārskata vienu pull request un atstāj vismaz trīs konkrētus komentārus pie konkrētām koda rindām.
- Komandas biedrs B: pārbauda atzaru nosaukumus, commit ziņojumus, `.gitignore`/noslēpumus, CI un pabeigtības definīciju; kartītē ieraksta piecas atzīmes.

Vārdus ieraksti sapulcē, kad grupa ir zināma.

### 18:00–20:00 — DoD un nākamā tikšanās

Vienojieties par pabeigtības definīciju, ierakstiet precīzu pārskata sapulces laiku un parakstiet sprinta kartīti.

## Pabeigtības definīcija (DoD)

Vienība ir pabeigta, ja:

1. Akcepta kritēriji pārbaudīti Android emulatorā vai ierīcē.
2. Kods atrodas saprotami nosauktā atzarā un iesniegts ar pull request.
3. Automatizētie testi un GitHub Actions ir sekmīgi.
4. Repozitorijā nav paroļu, tokenu vai lokālu konfigurācijas failu.
5. Izmaiņas ir pārskatītas, sapludinātas `main` un demonstrējamas dzīvajā.

## Darbs sprinta laikā

- Viena backlog vienība = viens GitHub Issue.
- Atzari, piemēram: `feature/PB-02-transaction-form` un `fix/PB-03-amount-validation`.
- Commit ziņojums apraksta paveikto, piemēram: `Add transaction amount validation`.
- Neveic visu tieši `main`; atstāj vismaz vienu īstu pull request komandas biedra koda apskatei.
- Dēļa kolonnas: `Backlog` → `Ready` → `In progress` → `Review` → `Done`.
- Nefiksē izdomātu komandas biedru darbu — komentāriem un pārbaudei jābūt reāliem.

## 15 minūšu pārskata sapulce

- **0:00–5:00:** Android lietotnē dzīvajā izpildi galveno scenāriju un parādi vismaz vienu kļūdas stāvokli.
- **5:00–10:00:** biedrs A ziņo par koda apskati, biedrs B — par procesa pārbaudi.
- **10:00–15:00:** kas pabeigts, kas nav un kāpēc; viena konkrēta izmaiņa nākamajam sprintam.

## Pierādījumi, kas jāsaglabā

- aizpildīta un parakstīta sprinta kartīte;
- repozitorija saite un sprinta beigu dēļa ekrānuzņēmums;
- regulāri commit datumi;
- pull request ar vismaz trim konkrētiem biedra komentāriem;
- procesa pārbaudes piecas atzīmes;
- sekmīga CI izpilde.

Uzdevumā minētie 05.10.–12.10.2026 datumi ir pagājuši. Pirms sapulces ar pasniedzēju jāapstiprina jaunais faktiskais sprinta sākuma un beigu datums.
