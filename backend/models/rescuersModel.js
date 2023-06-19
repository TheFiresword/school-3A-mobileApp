
class Rescuer{
    id;
    firstname;
    lastname;
    email;
    password;
    telephone;
    disponibility;
   
    constructor(firstname, lastname, email, password, telephone="NaN", disponibility=1){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.disponibility = disponibility;
    }
}

module.exports = Rescuer;