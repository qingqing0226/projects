import mysql.connector
import csv
from mysql.connector import errorcode

try:
    username = input("write the username to log in to mysql ")
    password = input("write the password to mysql: ")
    Dai_Hanna = mysql.connector.connect(host = "127.0.0.1" , user = username, password = password)
except mysql.connector.Error as Error:
    print("the username or the password you inserted are not correct please insert the correct password and username")
    print("if after inserting  the password and the username the program does not work please start the program again!")
    username = input("write the username to log in to mysql ")
    password = input("write the password to mysql: ")
    Dai_Hanna = mysql.connector.connect(host = "127.0.0.1" , user = username, password = password)

# a cursor to perform commands with
mycursor = Dai_Hanna.cursor()
# a command to display databases to show that the database is indeed created
try :
    mycursor.execute("use Dai_Hanna")
except mysql.connector.Error as Error:
    print("This database does not exist") 
    mycursor.execute("create database Dai_Hanna")
    mycursor.execute("use Dai_Hanna")
    print("The database is being created, this may take up to 5 minutes so please sit tight you will see the interface once its done creating and adding tables")
    print("Please DO NOT press anything while the tables and the database are being created!")

mycursor.execute("show tables")
fetcher = mycursor.fetchall()
tables = []
for  i in fetcher:
    for j in i:
        tables.append(j)

# The following if statements are used to create 4 tables if they do not exist in the database

if "adolescentbirthrate" not in tables:
    mycursor.execute("CREATE TABLE adolescentBirthRate (Country VARCHAR(100) NOT NULL ,Year int, Birthrate double)")
    with open ("adolescentBirthRate.csv","r") as file:
        iterator = csv.reader(file)
        next(iterator)
        for line in iterator:
            values_birthrate = (line[0], line[1], line[3])
            submission1 = "INSERT INTO adolescentBirthRate VALUES (%s, %s, %s)"
            mycursor.execute(submission1,values_birthrate)
            Dai_Hanna.commit()


if "lifeexpectancyatbirth" not in tables:
    mycursor.execute("CREATE TABLE lifeExpectancyAtBirth (Country VARCHAR(100) NOT NULL ,Year int, Expectancy int)")
    with open ("lifeExpectancyAtBirth.csv","r") as file:
         browser = csv.reader(file)
         next(browser)
         for line in browser:
             values_expectancy = (line[0], line[1], line[4])
             submission2 = "INSERT INTO lifeExpectancyAtBirth VALUES (%s, %s, %s)"
             mycursor.execute(submission2,values_expectancy)
             Dai_Hanna.commit()

if "maternalmortalityratio" not in tables:
    mycursor.execute("CREATE TABLE maternalMortalityRatio (Country VARCHAR(100) NOT NULL ,Year int, Mortality VARCHAR(100))")
    with open ("maternalMortalityRatio.csv","r") as file:
         browser = csv.reader(file)
         next(browser)
         for line in browser:
             values_mortality = (line[0], line[1], line[3])
             submission2 = "INSERT INTO maternalMortalityRatio VALUES (%s, %s, %s)"
             mycursor.execute(submission2,values_mortality)
             Dai_Hanna.commit()

if "reproductiveagewomen" not in tables:
    mycursor.execute("CREATE TABLE reproductiveAgeWomen (Country VARCHAR(100) NOT NULL ,Tooltip VARCHAR(500), Year int, Reproductive_Rate double)")
    with open ("reproductiveAgeWomen.csv","r") as file:
         browser = csv.reader(file)
         next(browser)
         for line in browser:
             values_reproductive = (line[0],line[1], line[2], line[3])
             submission2 = "INSERT INTO reproductiveAgeWomen VALUES (%s, %s, %s,%s)"
             mycursor.execute(submission2,values_reproductive)
             Dai_Hanna.commit()

# life expectancy against birth rate per 1000 women
def expectancy_vs_adolescent():
    try:
        mycursor.execute("show tables")
        fetcher = mycursor.fetchall()
        tables_func = []
        for  i in fetcher:
            for j in i:
                tables_func.append(j)
        if "expectancy_vs_adolescent" in tables_func:
            mycursor.execute("DROP VIEW expectancy_vs_adolescent")
        help_expectancy_adolescent()
    except mysql.connector.Error as Error:
        print("The input you inserted is invalid")
    except mysql.connector.ProgrammingError as Error:
        print("The input you inserted is invalid")

# the help method of expectancy_vs_adolescent()
def help_expectancy_adolescent():
    country = input("Write the name of the country you wish to see: ")
    country = capitalize_words(country)
    mycursor.execute(f"CREATE VIEW Expectancy_vs_adolescent  AS SELECT avg(Expectancy), avg(Birthrate) FROM lifeExpectancyAtBirth, adolescentBirthRate WHERE adolescentBirthRate.Country like '%{country}%' AND lifeExpectancyAtBirth.Country like '%{country}%' ")
    mycursor.execute("SELECT * FROM Expectancy_vs_adolescent ")
    get_result = mycursor.fetchone()
    if get_result[0] == None:
        print("This country doesn't exist in the database. Please try again.")
        expectancy_vs_adolescent()
    else:
        print("Life expectancy \t Birthrate per 1000 women")
        print("{:<15} \t {}".format(get_result[0], get_result[1]))

# birth rate per 1000 women against maternal mortality rate
def maternal_vs_adolescent():
    mycursor.execute(f"SELECT * FROM adolescentBirthRate INNER JOIN maternalMortalityRatio ON adolescentBirthRate.Country = maternalMortalityRatio.Country WHERE adolescentBirthRate.Year = maternalMortalityRatio.Year ")
    get_result = mycursor.fetchall()
    return get_result

# help method of maternal_vs_adolescent()
def loop(variable):
    first_list = []
    second_list = []
    third_list = []
    for i in range(0, 700):
        first_list.append(variable[i])
    for i in range(700,1400):
        second_list.append(variable[i])
    for i in range(1400, 2101):
        third_list.append(variable[i])
    print("The contents of the list are too big to be shown in one go")
    print("They were divided into three groups")
    print("Here is a table of the first element in each group:\n")
    print("{:<11} {} {:<9} {}".format("country", "year", "birthrate", "mortality_ratio"))
    print("{:<11} {} {:<9} {}".format(first_list[0][0], first_list[0][1], first_list[0][2], first_list[0][5]))
    print("{:<11} {} {:<9} {}".format(second_list[0][0], second_list[0][1], second_list[0][2], second_list[0][5]))
    print("{:<11} {} {:<9} {}".format(third_list[0][0], third_list[0][1], third_list[0][2], third_list[0][5]))
    print("\nNOTE: birthrate = birthrate per 1000 women")
    print("      mortality_ratio = mortality ratio per 100 000 live births")
    print("\nThe first elemnt in each of the lists serves as a good reference that help your choice in case you want a spcific country")
    help_loop(first_list, second_list, third_list)

# help method of loop() 
def help_loop(list1, list2, list3):
    running = True
    while running:
        choice = input("\nChoose a number between (1,2,3), any other option will take you back to the main menu: ")
        if choice == "1":
            print("{:<37} {} {:<9} {}".format("country", "year", "birthrate", "mortality_ratio"))
            for i in list1:
                print("{:<37} {} {:<9} {}".format(i[0], i[1], i[2], i[5]))
        elif choice == "2":
            print("{:<32} {} {:<9} {}".format("country", "year", "birthrate", "mortality_ratio"))
            for i in list2:
                print("{:<32} {} {:<9} {}".format(i[0], i[1], i[2], i[5]))
        elif choice == "3":
            print("{:<52} {} {:<9} {}".format("country", "year", "birthrate", "mortality_ratio"))
            for i in list3:
                print("{:<52} {} {:<9} {}".format(i[0], i[1], i[2], i[5]))
        else :
            print("You chose to exit, returning to the main menu....")
            running = False

# capitalize first letter of each word in a string
def capitalize_words(string):
    temp = [w.capitalize() for w in string.split()]
    new_string = ""
    for w in temp:
        new_string += (w+' ')
    new_string = new_string[0:len(new_string)-1]  # remove the last space
    return new_string

# birth rate per 1000 women against women of reproductive age who have their need for family planning satisfied with modern methods (%)
def reproductive_vs_adolescent():
    try:
        country = input("Write the name of the country you wish to see: ")
        country = capitalize_words(country)
        mycursor.execute(f"Select adolescentBirthRate.Country, avg(Birthrate), reproductiveagewomen.Reproductive_Rate FROM adolescentBirthRate, reproductiveagewomen Where reproductiveagewomen.Country like '%{country}%' AND adolescentBirthRate.Country like '%{country}%'")
        get_result = mycursor.fetchone()
        if get_result[0] == None:
            print("This country doesn't exist in the database. Please try again.")
            reproductive_vs_adolescent()
        else:
            print("Country: " + get_result[0])
            print("Adolescent birth rate (per 1000 women aged 15-19 years): ", get_result[1])
            print("Married or in-union women of reproductive age who have their need for family planning satisfied with modern methods (%): ", get_result[2])
    except mysql.connector.Error as Error:
        print("the input you inserted is invalid")
    except mysql.connector.ProgrammingError as Error:
        print("the input you inserted is invalid")

# average birthrate for each country
def adolescent():
    mycursor.execute("select country, avg(Birthrate) from adolescentBirthRate GROUP BY country")
    get_result = mycursor.fetchall()
    print("{:<54} {}".format("country", "average birthrate"))
    for i in get_result:
        print("{:<54} {}".format(i[0], i[1]) )

# women of reproductive age who have their need for family planning satisfied with modern methods (%)
# against maternal mortality per 100 000 live birth
def maternal_vs_reproductive():
    mycursor.execute("select reproductiveAgeWomen.Country, reproductiveAgeWomen.Reproductive_Rate, maternalMortalityRatio.Mortality from reproductiveAgeWomen, maternalMortalityRatio where reproductiveAgeWomen.Country = maternalMortalityRatio.Country and reproductiveAgeWomen.Year = maternalMortalityRatio.Year")
    result = mycursor.fetchall()
    print("\nNOTE:")
    print("Percentage = women of reproductive age who have their need for family planning satisfied with modern methods (%)")
    print("Mortality = maternal mortality per 100 000 live birth")
    print("----------------------------------------------------------------------------------------------------------------")
    print("{:<54} {:<20} {}".format("Country", "Percentage", "Mortality"))
    for r in result:
        print("{:<54} {:<20} {}".format(r[0], r[1], r[2]))

# present the main menu
def interface( ):
    try:
        print("\n1. Compare life expectancy of the child against the adolescent Birthrate in a country.")
        print("2. Compare the maternal mortality rate against the adolescent births in a country")
        print("3. Compare the reproductive age of women against the adolescent birth rate")
        print("4. Display the average adolescent birthrate grouped by country")
        print("5. Compare maternal mortality rate against reproductive age women")
        print("6. you exit the menu")
        variable = int(input("Which of these previous options would you like to choose ? " ))
        if variable == 1:
            expectancy_vs_adolescent()
            input("\nPress any button to go back to the previous menu: ")
            interface()
        elif variable == 2:
            loop(maternal_vs_adolescent())
            interface()
        elif variable == 3:
            reproductive_vs_adolescent()
            input("\nPress any button to go back to the previous menu: ")
            interface()
        elif variable == 4:
            adolescent()
            input("\nPress any button to go back to the previous menu: ")
            interface()
        elif variable == 5:
            maternal_vs_reproductive()
            input("\nPress any button to go back to the previous menu: ")
            interface()
        elif variable == 6:
            return "The interface has been terminated"
        else:
            print("The value you inserted does not correspond to anything in the main menu \nyou will be shown the main menu again \nplease read the options and follow them carefully!")
            interface()
    except ValueError as Error:
        print("The input you inserted is invalid\n You will be taken to the main menu please read the option carefully")
        interface()
interface()