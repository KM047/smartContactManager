document
    .getElementById("image_file_input")
    .addEventListener("change", function (e) {
        let file = e.target.files[0];

        console.log(file);

        let reader = new FileReader();
        reader.onload = function (e) {
            document
                .getElementById("upload_image_preview")
                .setAttribute("src", reader.result);
        };
        reader.readAsDataURL(file);
    });
