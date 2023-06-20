const crypto = require('crypto');
const bcrypt = require('bcrypt');

const generateSecretKey = ()=>{
    const keyLen = 32;
    const secretKey = crypto.randomBytes(keyLen).toString('hex');
    return secretKey;
}

const secretKey = generateSecretKey();
//console.log('Secret key is', secretKey);
module.exports = secretKey;