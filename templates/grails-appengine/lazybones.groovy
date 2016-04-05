def props = [:]
props.group = ask("Define value for 'group' [org.example]: ", "org.example", "group")
props.version = ask("Define value for 'version' [0.1]: ", "0.1", "version")
props.projectId = ask("Define value for AppEngine 'projectId' [your-app]: ", "your-app", "projectId")
props.credentialsJson = ask("Define value for 'credentialsJson' [../project-credentials.json]: ", "../project-credentials.json", "credentialsJson")

processTemplates "build.gradle", props
