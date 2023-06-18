
class Rescuer{
    id;
    firstname;
    lastname;
    email;
    telephone;
    disponibility;
    password;
    
    constructor(id, firstname, lastname, email, password, telephone="", disponibility=true){
        this.id=id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.telephone = telephone;
        this.disponibility = disponibility;
    }
}

module.exports = Rescuer;