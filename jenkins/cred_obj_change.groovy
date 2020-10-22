import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl

def changePassword = { username, new_password ->
    def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
        com.cloudbees.plugins.credentials.common.StandardUsernameCredentials.class,
        Jenkins.instance
    )
    printAllMethods(creds)
    println "creds: ${creds}"
    // def c = creds.findResult { it.username == username ? it : null }
    def c = creds.findResult { it.id == credid ? it : null }
    println "cr: ${c}"
    if ( c ) {
        println "found credential ${c.id} for username ${c.username}"

        def credentials_store = Jenkins.instance.getExtensionList(
            'com.cloudbees.plugins.credentials.SystemCredentialsProvider'
            )[0].getStore()

        def result = credentials_store.updateCredentials(
            com.cloudbees.plugins.credentials.domains.Domain.global(), 
            c, 
            new UsernamePasswordCredentialsImpl(c.scope, c.id, c.description, c.username, new_password)
            )

        if (result) {
            println "password changed for ${username}" 
        } else {
            println "failed to change password for ${username}"
        }
    } else {
      println "could not find credential for ${username}"
    }
}
def printAllMethods( obj ){
    if( !obj ){
		println( "Object is null\r\n" );
		return;
    }
	if( !obj.metaClass && obj.getClass() ){
        printAllMethods( obj.getClass() );
		return;
    }
	def str = "class ${obj.getClass().name} functions:\r\n";
	obj.metaClass.methods.name.unique().each{ 
		str += it+"(); "; 
	}
	println "${str}\r\n";
}

changePassword('testuser1', pwd)
