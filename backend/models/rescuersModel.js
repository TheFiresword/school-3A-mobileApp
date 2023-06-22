
class Rescuer{
    id;
    firstname;
    lastname;
    email;
    password;
    telephone;
    disponibility;
    description;
    tokenFirebase;
   
    constructor(firstname, lastname, email, password, telephone="NaN", disponibility=1, tokenFirebase="Nan"){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.disponibility = disponibility;
        this.tokenFirebase = tokenFirebase;
    }
}

module.exports = Rescuer;