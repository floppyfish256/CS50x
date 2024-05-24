import os

from cs50 import SQL
from flask import Flask, flash, redirect, render_template, request, session
from flask_session import Session
from tempfile import mkdtemp
from werkzeug.exceptions import default_exceptions, HTTPException, InternalServerError
from werkzeug.security import check_password_hash, generate_password_hash
from datetime import datetime

from helpers import apology, login_required, lookup, usd

# Configure application
app = Flask(__name__)

# Ensure templates are auto-reloaded
app.config["TEMPLATES_AUTO_RELOAD"] = True


# Ensure responses aren't cached
@app.after_request
def after_request(response):
    response.headers["Cache-Control"] = "no-cache, no-store, must-revalidate"
    response.headers["Expires"] = 0
    response.headers["Pragma"] = "no-cache"
    return response


# Custom filter
app.jinja_env.filters["usd"] = usd

# Configure session to use filesystem (instead of signed cookies)
app.config["SESSION_FILE_DIR"] = mkdtemp()
app.config["SESSION_PERMANENT"] = False
app.config["SESSION_TYPE"] = "filesystem"
Session(app)

# Configure CS50 Library to use SQLite database
db = SQL("sqlite:///finance.db")

# Make sure API key is set
if not os.environ.get("API_KEY"):
    raise RuntimeError("API_KEY not set")


@app.route("/")
@login_required
def index():
    """Show portfolio of stocks"""
    user_cash = db.execute("SELECT cash FROM users WHERE id = ? ", session["user_id"])[0]["cash"]
    info = db.execute("SELECT symbol, SUM(shares) as shares FROM stock WHERE user = ? GROUP BY symbol HAVING (SUM(shares)) > 0;", session["user_id"])
    invested = 0
    for stock in info:
        quote = lookup(stock["symbol"])
        stock["name"] = quote["name"]
        stock["price"] = quote["price"]
        stock["total"] = stock["price"] * stock["shares"]
        invested = invested + stock["total"]

    total = invested + user_cash

    return render_template(
        "index.html", info=info, user_cash=user_cash, total=total
    )


@app.route("/buy", methods=["GET", "POST"])
@login_required
def buy():
    if request.method == "POST":

        #Require that a user input a stock’s symbol, implemented as a text field whose name is symbol.
        symbol = request.form.get("symbol")

        #Odds are you’ll want to call lookup to look up a stock’s current price.
        price = lookup(symbol)
        shares = request.form.get("shares")

        #Odds are you’ll want to SELECT how much cash the user currently has in users.
        user_cash = db.execute("SELECT cash FROM users WHERE id = ? ", session["user_id"])[0]["cash"]

        #Render an apology if the input is blank or the symbol does not exist (as per the return value of lookup).
        if not symbol:
            return apology("a valid symbol must be provide", 400)
        elif price is None:
            return apology("must provide valid symbol", 400)

        #Require that a user input a number of shares, implemented as a text field whose name is shares.
        try:
            shares = int(shares)
            #Render an apology if the input is not a positive integer.
            if shares < 1:
                return apology("share must be a positive integer", 400)
        except ValueError:
            return apology("share must be a positive integer", 400)

        #Render an apology, without completing a purchase, if the user cannot afford the number of shares at the current price.
        shares_price = shares * price["price"]
        if user_cash < (shares_price):
            return apology("not enough cash", 400)
        else:
            now = datetime.now()
            dt_string = now.strftime("%d/%m/%Y %H:%M:%S")
            db.execute("UPDATE users SET cash = cash - ? WHERE id = ?", shares_price, session["user_id"])
            db.execute("CREATE TABLE IF NOT EXISTS stock (user INTEGER NOT NULL, symbol TEXT NOT NULL, name TEXT NOT NULL, shares INTEGER NOT NULL, price FLOAT NOT NULL, time TEXT NOT NULL)")
            db.execute("INSERT INTO stock (user, symbol, name, shares, price, time) VALUES (?, ?, ?, ?, ?, ?)", session["user_id"], symbol, price["name"], shares, price["price"], dt_string)

            flash("Bought!")

            #When a purchase is complete, redirect the user back to the index page.
            return redirect("/")

    else:
        return render_template("buy.html")


@app.route("/history")
@login_required
def history():
    """Show history of transactions"""
    history = db.execute("SELECT * FROM stock WHERE user = ?", session["user_id"])
    return render_template("history.html", history=history)


@app.route("/login", methods=["GET", "POST"])
def login():
    """Log user in"""

    # Forget any user_id
    session.clear()

    # User reached route via POST (as by submitting a form via POST)
    if request.method == "POST":

        # Ensure username was submitted
        if not request.form.get("username"):
            return apology("must provide username", 403)

        # Ensure password was submitted
        elif not request.form.get("password"):
            return apology("must provide password", 403)

        # Query database for username
        rows = db.execute("SELECT * FROM users WHERE username = ?", request.form.get("username"))

        # Ensure username exists and password is correct
        if len(rows) != 1 or not check_password_hash(rows[0]["hash"], request.form.get("password")):
            return apology("invalid username and/or password", 403)

        # Remember which user has logged in
        session["user_id"] = rows[0]["id"]

        # Redirect user to home page
        return redirect("/")

    # User reached route via GET (as by clicking a link or via redirect)
    else:
        return render_template("login.html")


@app.route("/logout")
def logout():
    """Log user out"""

    # Forget any user_id
    session.clear()

    # Redirect user to login form
    return redirect("/")


@app.route("/quote", methods=["GET", "POST"])
@login_required
def quote():
    """Get stock quote."""
    if request.method == "POST":
        #Require that a user input a stock’s symbol, implemented as a text field whose name is symbol.
        quote = lookup(request.form.get("symbol"))
        if quote is None:
            return apology("must provide valid symbol", 400)
        else:
            #Submit the user’s input via POST to /quote.
            return render_template(
                "quoted.html",
                name=quote["name"],
                symbol=quote["symbol"],
                price=quote["price"],
            )
    else:
        return render_template("quote.html")


@app.route("/register", methods=["GET", "POST"])
def register():
    """Register user"""
    if request.method == "POST":

        #Require that a user input a username, implemented as a text field whose name is username.
        username = request.form.get("username")
        #Require that a user input a password, implemented as a text field whose name is password.
        password = request.form.get("password")
        #Same password again, implemented as a text field whose name is confirmation.
        confirmation = request.form.get("confirmation")
        rows = db.execute("SELECT * FROM users WHERE username = ?", username)

        #Render an apology if the user’s input is blank or the username already exists.
        if not username:
            return apology("must provide username", 400)
        elif len(rows) != 0:
            return apology("username already exists", 400)
        elif not password:
        #Render an apology if either input is blank or the passwords do not match.
            return apology("must provide password", 400)
        elif not request.form.get("confirmation"):
            return apology("must provide a confirmation password", 400)
        elif not password == confirmation:
            return apology("passwords must match", 400)

        else:
            hash = generate_password_hash(password, method="pbkdf2:sha256", salt_length=8)
            db.execute("INSERT INTO users (username, hash) VALUES (?, ?) ", username, hash)
            return redirect("/")

    else:
        return render_template("register.html")


@app.route("/sell", methods=["GET", "POST"])
@login_required
def sell():
    """Sell shares of stock"""
    if request.method == "POST":
        symbol = request.form.get("symbol")
        shares = request.form.get("shares")
        try:
            shares = int(shares)
            if shares < 1:
                return apology("shares must be a positive integer")
        except ValueError:
            return apology("shares must be a positive integer")
        if not symbol:
            return apology("missing symbol")

        stocks = db.execute("SELECT SUM(shares) as shares FROM stock WHERE user = ? AND symbol = ?;", session["user_id"], symbol,)[0]

        if shares > stocks["shares"]:
            return apology("Not enough shares to sell")
        price = lookup(symbol)["price"]
        shares_value = price * shares
        now = datetime.now()
        dt_string = now.strftime("%d/%m/%Y %H:%M:%S")
        db.execute("INSERT INTO stock (user, symbol, name, shares, price, time) VALUES (?, ?, ?, ?, ?, ?)", session["user_id"], symbol, lookup(symbol)["name"], -shares, price, dt_string)

        db.execute("UPDATE users SET cash = cash + ? WHERE id = ?", shares_value, session["user_id"])

        flash("Sold!")
        return redirect("/")
    else:
        stocks = db.execute("SELECT symbol FROM stock WHERE user = ? GROUP BY symbol", session["user_id"])
        return render_template("sell.html", stocks=stocks)


def errorhandler(e):
    """Handle error"""
    if not isinstance(e, HTTPException):
        e = InternalServerError()
    return apology(e.name, e.code)


# Listen for errors
for code in default_exceptions:
    app.errorhandler(code)(errorhandler)
