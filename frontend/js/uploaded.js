export function uploadFile(file, onProgress) {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest(); // use XHR object to interact with the server
        const formData = new FormData();
        formData.append("file", file);
        
        xhr.open("POST", "http://localhost:8080/upload", true);
        const username = "user";
        const password = "1234";

        xhr.setRequestHeader("Authorization", "Basic " + btoa(`${username}:${password}`));
        
        xhr.upload.addEventListener("progress", (event) => {
            if (event.lengthComputable) {
                const percentComplete = (event.loaded / event.total) * 100;
                onProgress(percentComplete);
            }
        });

        xhr.onload = () => {
            if (xhr.status === 200) {
                const response = JSON.parse(xhr.responseText);
                resolve(response.url);
            } else {
                reject(new Error(xhr.statusText || "Upload error"));
            }
        };
        xhr.onerror = () => reject(new Error("Upload failed"));
        xhr.send(formData);
    });
}
