INSERT INTO "role" (nazwa) VALUES
('ADMIN'),
('PRACOWNIK'),
('WOLONTARIUSZ'),
('SEKRETARZ');

INSERT INTO rasy (typ_zwierzecia, rasa) VALUES
('Pies', 'Owczarek Niemiecki'),
('Pies', 'Labrador Retriever'),
('Kot', 'Brytyjski Krótkowłosy'),
('Kot', 'Dachowiec');

INSERT INTO uzytkownicy (email, haslo_hash, imie, nazwisko, rola_id) VALUES
('admin@schronisko.pl', 'hash_123', 'Anna', 'Nowak', 1),
('jan.kowalski@schronisko.pl', 'hash_456', 'Jan', 'Kowalski', 2),
('marta.wolontariusz@gmail.com', 'hash_789', 'Marta', 'Zielińska', 3),
('sekretariat@schronisko.pl', 'hash_000', 'Piotr', 'Wiśniewski', 4);

INSERT INTO zwierzeta (imie, wiek, waga, plec, rasa_id, status, opis, charakter) VALUES
('Burek', 5, 25.50, 'Samiec', 1, 'DO ADOPCJI', 'Duży, energiczny pies.', 'Przyjacielski, potrzebuje dużo ruchu.'),
('Luna', 2, 4.20, 'Samica', 3, 'KWARANTANNA', 'Spokojna kotka.', 'Nieśmiała, lubi ciszę.'),
('Maks', 8, 30.00, 'Samiec', 2, 'ADOPTOWANY', 'Bardzo łagodny biszkoptowy labrador.', 'Kocha dzieci i jedzenie.');

INSERT INTO historia_medyczna (zwierze_id, pracownik_id, opis_zabiegu) VALUES
(1, 2, 'Szczepienie przeciwko wściekliźnie oraz odrobaczenie.'),
(2, 2, 'Podstawowy przegląd weterynaryjny po przybyciu do schroniska.');

INSERT INTO rezerwacje_spacerow (wolontariusz_id, zwierze_id, data_spaceru, godzina_start, godzina_koniec, uwagi) VALUES
(3, 1, CURRENT_DATE + INTERVAL '1 day', '10:00:00', '11:30:00', 'Pies ciągnie na smyczy, uważać na inne psy.');

INSERT INTO raporty_operacyjne (data_raportu, sekretarz_id, typ_raportu, uwagi) VALUES
(CURRENT_DATE, 4, 'MIESIĘCZNY', 'Zwiększona liczba adopcji w tym miesiącu.');