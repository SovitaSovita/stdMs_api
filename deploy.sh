#!/bin/bash

# Prompt user if they have built the file
read -p "Have you built the file with Maven (Y/N)? " build_choice
case "$build_choice" in
    [Yy]* )
        # Local file path (keep original Maven-generated name)
        jar_file="target/STMS_API-0.0.1-SNAPSHOT.jar"

        # Check if the file exists
        if [ ! -f "$jar_file" ]; then
            echo "Jar file not found: $jar_file"
            exit 1
        fi

        # Server details
        server_username="sengkim"
        server_ip="209.146.62.96"
        server_port="22"
        server_path="/home/sengkim/stms_api"

        # Copy file to server
        scp -P "$server_port" "$jar_file" "$server_username@$server_ip:$server_path"

        # Check if the scp command was successful
        if [ $? -ne 0 ]; then
            echo "Failed to copy the file to the server."
            exit 1
        fi

        # Run the Java file with nohup on the server
        ssh -p "$server_port" "$server_username@$server_ip" "cd $server_path && nohup java -jar STMS_API-0.0.1-SNAPSHOT.jar > output.log 2>&1 &"

        # Check if the ssh command was successful
        if [ $? -ne 0 ]; then
            echo "Failed to run the Java file on the server."
            exit 1
        fi

        # Success message
        echo "File successfully copied and Java application started on the server."
        ;;
    * )
        echo "File not copied to the server. Please build the file using 'mvn clean package' first."
        ;;
esac
