-- Keep a log of any SQL queries you execute as you solve the mystery.
.tables;
.schema crime_scene_reports;
SELECT * FROM crime_scene_reports WHERE month = 7 AND day = 28 AND street = "Humphrey Street";

-- Time: 10:15 am, Place: Humphrey Street bakery. Interviews with three witnesses, mention bakery.
.schema interviews
SELECT * FROM interviews WHERE month = 7 AND day = 28 AND transcript LIKE "%bakery%";
-- Left the bakery parking lot within 10:15 to 10:25, withdrew money from ATM on Leggett Street on 7/28, taking earliest flight on 7/29, suspicious person called for half an hour

-- Car Suspects: Vanessa, Bruce, Barry, Luca, Sofia, Iman, Diana, Kelsey
SELECT name, bakery_security_logs.hour, bakery_security_logs.minute
FROM people JOIN bakery_security_logs
ON people.license_plate = bakery_security_logs.license_plate
WHERE bakery_security_logs.year = 2021
AND bakery_security_logs.month = 7
AND bakery_security_logs.day = 28
AND bakery_security_logs.activity = 'exit'
AND bakery_security_logs.hour = 10
AND bakery_security_logs.minute >= 15
AND bakery_security_logs.minute <= 25;

-- ATM Suspects: Bruce, Diana, Brooke, Kenny, Iman, Luca, Taylor, Benista
SELECT name, atm_transactions.amount
FROM people JOIN bank_accounts
ON people.id = bank_accounts.person_id JOIN atm_transactions
ON bank_accounts.account_number = atm_transactions.account_number
WHERE atm_transactions.year = 2021
AND atm_transactions.month = 7
AND atm_transactions.day = 28
AND atm_transactions.atm_location = 'Leggett Street'
AND atm_transactions.transaction_type = 'withdraw';

-- Flight Suspects: Doris, Sofia, Bruce, Edward, Kelsey, Taylor, Kenny, Luca
SELECT flights.id, full_name, city, flights.hour, flights.minute
FROM airports
JOIN flights
ON airports.id = flights.destination_airport_id
WHERE flights.origin_airport_id = (SELECT id FROM airports WHERE city = 'Fiftyville')
AND flights.year = 2021
AND flights.month = 7
AND flights.day = 29
ORDER BY flights.hour, flights.minute;
-- Earliest flight at 8:20 to New York City
SELECT passengers.flight_id, name, passengers.passport_number, passengers.seat
FROM people
JOIN passengers
ON people.passport_number = passengers.passport_number
JOIN flights
ON passengers.flight_id = flights.id
WHERE flights.year = 2021
AND flights.month = 7
AND flights.day = 29
AND flights.hour = 8
AND flights.minute = 20

-- Receiver: Robin, who called Bruce
SELECT name, phone_calls.duration
FROM people
JOIN phone_calls
ON people.phone_number = phone_calls.receiver
WHERE phone_calls.year = 2021
AND phone_calls.month = 7
AND phone_calls.day = 28
AND phone_calls.duration <= 60
ORDER BY phone_calls.duration;

-- Caller: Kelsey, Carina, Taylor, Bruce, Diana, Kelsey, Sofia, Benista, Kenny, Kathryn
SELECT name, phone_calls.duration
FROM people
JOIN phone_calls
ON people.phone_number = phone_calls.caller
WHERE phone_calls.year = 2021
AND phone_calls.month = 7
AND phone_calls.day = 28
AND phone_calls.duration <= 60
ORDER BY phone_calls.duration;

-- The THEIF is Bruce
-- The city the theif escaped to is New York City
-- The ACCOMPLICE is Robin