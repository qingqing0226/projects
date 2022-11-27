import csv
import mysql.connector
from mysql.connector import errorcode

cnx = mysql.connector.connect(user='root',
                              password='root',
                              host='127.0.0.1',
                              port='3308'
                              )
DB_NAME = 'DAI'
cursor = cnx.cursor()

# read a csv file, set headers and rows of data
def read_file(name, headers, rows):
    with open(name, 'r') as csvfile:
        reader = csv.reader(csvfile)
        headers = next(reader)
        for row in reader:
            rows.append(row)

def create_database(cursor, DB_NAME):
    try:
        cursor.execute("CREATE DATABASE IF NOT EXISTS {} DEFAULT CHARACTER SET 'utf8'".format(DB_NAME))
    except mysql.connector.Error as err:
        print("Failed creating database: {}".format(err))
        exit(1)

def create_table(cursor, name, attributes, queries):
    query = "CREATE TABLE IF NOT EXISTS `{}` ({}) ENGINE=InnoDB".format(name, attributes)
    try:
        print("Creating table: " + name)
        cursor.execute(query)
        insert_into_table(cursor, queries)
    except mysql.connector.Error as err:
        if err.errno == errorcode.ER_TABLE_EXISTS_ERROR:
            print("already exists.")
        else:
            print(err.msg)
    else:
        print("OK")

# make insert queries
def insert_query(table, attributes, rows):
    queries = []
    for row in rows:
        values = ""
        for col in row:
            if col == "NA":
                values += "NULL, "
            elif "'" in col:  # use backslash to escape single quote
                single_quote = col.index("'")
                col = col[0:single_quote] + "\\\'" + col[single_quote+1:]
                values += ("'{}', ".format(col))
            else:
                values += ("'{}', ".format(col))
        values = values[0:len(values)-2]  # remove the last comma and space
        if values[0:4] != "NULL":  # name is primary key, cannot be NULL
            query = "INSERT INTO {} ({}) VALUES ({})".format(table, attributes, values)
        queries.append(query)
    return queries

# populate the table with data
def insert_into_table(cursor, queries):
    for query in queries:
        try:
            cursor.execute(query)
        except mysql.connector.Error as err:
            err
        else:
            cnx.commit()

# capitalize first letter of each word in a string
def capitalize_words(string):
    temp = [w.capitalize() for w in string.split()]
    new_string = ""
    for w in temp:
        new_string += (w+' ')
    new_string = new_string[0:len(new_string)-1]  # remove the last space
    return new_string

# if any key is pressed followed by ENTER, return True; otherwise, return False
def return_menu():
    any_key = input("\nPress any key followed by ENTER to return to main menu ")
    if any_key != "":
        return True
    return False


create_database(cursor, DB_NAME)
print("Database {} created successfully.".format(DB_NAME))
cnx.database = DB_NAME
cursor.execute("USE {}".format(DB_NAME)) 
''' 1. Parse the files and import the data in the database
        create table - "planets"
        PRIMARY KEY: name
'''
headers = []
rows = []
read_file('planets.csv', headers, rows)
attributeTypes = "`name` VARCHAR(20) NOT NULL, `rotation_period` SMALLINT, `orbital_period` SMALLINT, `diameter` MEDIUMINT, `climate` VARCHAR(30), `gravity` VARCHAR(50), `terrain` VARCHAR(50), `surface_water` FLOAT, `population` BIGINT, PRIMARY KEY (`name`)"
attributes = "name, rotation_period, orbital_period, diameter, climate, gravity, terrain, surface_water, population"
queries = insert_query("planets", attributes, rows)
create_table(cursor, 'planets', attributeTypes, queries)
# note: got error message "duplicate entry 'Ord Mantell' for key 'planets.PRIMARY'"
#       but the table has been created successfully and 'name' is primary key
#       so, the message can be ignored.

'''     create table - "species"
        PRIMARY KEY: name
        FOREIGN KEY: homeworld REFERENCES planets (name)
'''
headers = []
rows = []
read_file('species.csv', headers, rows)
attributeTypes = "`name` VARCHAR(20) NOT NULL, `classification` VARCHAR(20), `designation` VARCHAR(20), `average_height` SMALLINT, `skin_colors` VARCHAR(50), `hair_colors` VARCHAR(50), `eye_colors` VARCHAR(50), `average_lifespan` VARCHAR(20), `language` VARCHAR(20), `homeworld` VARCHAR(20), PRIMARY KEY (`name`), FOREIGN KEY (`homeworld`) REFERENCES planets (`name`)"
attributes = "name,classification,designation,average_height,skin_colors,hair_colors,eye_colors,average_lifespan,language,homeworld"
queries = insert_query("species", attributes, rows)
create_table(cursor, 'species', attributeTypes, queries)
  

''' 2. Queries
    present a menu with options [1-6]
    [1-5] queries
    [6] terminate the program
'''
running = True
while running:           
    print("Welcome to the database " + DB_NAME)
    print("Tables: planets, species")
    print("Main Menu:")
    print("[1] List all planets")
    print("[2] Search for planet details")
    print("[3] Search for species with height higher than given number")
    print("[4] What is the most likely desired climate of the given species?")
    print("[5] What is the average lifespan per species classification?")
    print("[6] Exit")
    user_input = input('Enter a number: ')
    if user_input == '1':
        try:
            cursor.execute("SELECT name FROM planets")
        except mysql.connector.Error as err:
            print(err.msg)
        else:
            result = cursor.fetchall()
            for row in result:
                print(row[0])
            if return_menu():
                continue
    elif user_input == '2':
        name = input("Enter planet name: ")
        # capitalize first letter of each word
        name = capitalize_words(name)
        try:
            cursor.execute("SELECT * FROM planets WHERE name='{}'".format(name))
        except mysql.connector.Error as err:
            print(err.msg)
        except Exception as e:
            print("Planet" + name + " does not exist.")
        else:
            # make (attribute,value) pairs
            attributes = "name rotation_period orbital_period diameter climate gravity terrain surface_water population"
            attributes = attributes.split()
            row = cursor.fetchone()
            pairs = tuple(zip(attributes, row))
            # print the pairs
            for pair in pairs:
                print(pair[0] + ": " + str(pair[1]))
            if return_menu():
                continue
    elif user_input == '3':
        height = input("Enter height: ")
        try:
            cursor.execute("SELECT name, average_height FROM species WHERE average_height > {}".format(height))
        except mysql.connector.Error as err:
            print(err.msg)
        else:
            # list name and average_height of the species higher than given number
            print("\n{:<15}{:<15}\n".format("name", "average_height"))
            result = cursor.fetchall()
            for row in result:
                for col in row:
                    print("{:<15}".format(str(col)), end='')
                print("\n")
            if return_menu():
                continue
    elif user_input == '4':
        species_name = input("Enter species name: ")
        if "'" in species_name:
            single_quote = species_name.index("'")
            species_name = species_name[0:single_quote] + "\\\'" + species_name[single_quote+1:]
        species_name = capitalize_words(species_name)
        # find the species' most desired climate by joining two tables with species.homeworld = planets.name
        try:
            cursor.execute("SELECT climate FROM planets join species on species.homeworld = planets.name WHERE species.name='{}'".format(species_name))
        except mysql.connector.Error as err:
            print(err.msg)
        except Exception as e:
            print("Cannot find the most desired climate.")
        else:
            result = cursor.fetchone()
            climate = result[0] if result != None else None
            # if climate is not NA, print its value
            if climate != None:
                print("The most likely desired climate is: " + climate)
                if return_menu():
                    continue
            else:
                print("The most likely desired climate is unknown.")
                if return_menu():
                    continue

    elif user_input == '5':
        try:
            # the rows that contain NA values will be ignored by using 'LIMIT 5' in the query
            cursor.execute("SELECT classification, AVG(average_lifespan) FROM species GROUP BY classification LIMIT 5")
        except mysql.connector.Error as err:
            print(err.msg)
        else:
            # print average_lifespan for each classification
            print("classification AVG(average_lifespan)")
            result = cursor.fetchall()
            for row in result:
                print("{:<14} {:>21}".format(row[0],row[1]))
            if return_menu():
                continue
    elif user_input == '6':
        running = False
    else:
        print("Invalid input")
        if return_menu():
            continue

cursor.close()
cnx.close()

