const db_connection_details ={
  host: process.env.DATABASE_URL,
  user: process.env.DATABASE_USER,
  port: process.env.DATABASE_PORT,
  password: process.env.DATABASE_PASSWD,
  database: process.env.DATABASE_NAME
}

const app_config_details = {
  port : process.env.APP_PORT || 3000
}

module.exports = {db_connection_details, app_config_details}