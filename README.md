# security-project

## Setting up the database

This project requires that the server host is also running a mySQL database. The file has been included in the root directory of the project.

One file needs to be modified - DatabaseAccountRepository.java. Specifically, the two variables named Username and Password.
These two variables need to be set to the username and password of an account with privileges to modify databases.

The database also needs to be uploaded. To upload the database to your mySQL server, run this command:
mysql -u [username] -p[password] SecurityProject < casinodata.sql

That is, for someone with the account 'root' and password 'swordfish', the command would look like this:
mysql -u root -pswordfish SecurityProject < casinodata.sql

Of course, this command needs to be run from a folder that can run mysql, and has access to the file. It's possible one or both needs the full path.