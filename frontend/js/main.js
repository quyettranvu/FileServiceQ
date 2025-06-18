import { uploadFile } from "./uploaded.js";

const form = document.getElementById("uploadForm");
const fileInput = document.getElementById("fileInput");
const linkContainer = document.getElementById("fileLink");
const progressBar = document.getElementById("uploadProgress");
const status = document.getElementById("uploadStatus");

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const files = Array.from(fileInput.files);
    linkContainer.innerHTML = "";
    progressBar.style.display = "none";
    progressBar.value = 0;

    if (!files.length) {
      status.content = "Please select at least one file to upload.";
      return;
    }

    const allowedTypes = ["image/jpeg", "image/png", "application/pdf"];
    const maxSizeMB = 5;

    const invalidFiles = files.filter(
      (file) =>
        !allowedTypes.includes(file.type) || file.size > maxSizeMB * 1024 * 1024
    );
    
    if (invalidFiles.length > 0) {
      status.textContent =
        "Invalid file type or size. Only JPEG, PNG, and PDF files are allowed.";
      return;
    }

    status.textContent = "Uploading...";
    progressBar.style.display = "block";

    for (let i = 0; i < files.length; i++) {
        const file = files[i];

        try {
            progressBar.value = 0;
            const uploadedUrl = await uploadFile(file, (progress) => {
              progressBar.value = progress;
            });

           const a = document.createElement("a");
            a.href = uploadedUrl;
            a.target = "_blank";
            a.textContent = `✔️ ${file.name}`;
            a.download =file.name; //trigger download on click
            linkContainer.appendChild(a);
            linkContainer.appendChild(document.createElement("br"));
        } catch (error) {
            const errorText = document.createElement("div");
            errorText.textContent = `❌ Failed to upload ${file.name}: ${error.message}`;
            linkContainer.appendChild(errorText);
        }
    }

    status.textContent = "All uploads completed.";
})