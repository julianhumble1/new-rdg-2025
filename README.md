## RDG-2025 Spring / React.js Project

This project is a web application for an amateur theatre group I have been part of for many years. It is a migration/rebuild of an existing project made in 2008 in Ruby on Rails, which can be visited currently at rdg.org. There are about 60 years of production and actor data, and the aim is to make a comprehensive, easily navigable and easily updatable database interface (as well as giving it a much needed makeover), while also giving me more practice with the Spring framework.

### Project Structure

The overall structure of the project is displayed by the following:

![architecture diagram](./images/architecture-diagram.png)

Given I am working with existing data, my first step was to examine and understand the structure of the relational database, and the core of the data follows this structure:

![database structure](./images/db-structure.png)

As well as this, there are to two types of users that can be logged in to the application, with 'User' and 'Admin' roles.

### Development process so far


