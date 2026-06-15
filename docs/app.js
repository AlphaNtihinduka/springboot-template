const requestSample = {
    username: "platform.owner",
    email: "platform.owner@bilimy.com",
    fullName: "Platform Owner",
    role: "PLATFORM_ADMIN",
    status: "ACTIVE",
    permissions: [
        "PLATFORM_USERS_READ",
        "PLATFORM_USERS_WRITE",
        "SERVICES_DEPLOY"
    ]
};

const requestSampleNode = document.getElementById("request-sample");
const copyButton = document.getElementById("copy-request");
const requestJson = JSON.stringify(requestSample, null, 2);

requestSampleNode.textContent = requestJson;

copyButton.addEventListener("click", async () => {
    await navigator.clipboard.writeText(requestJson);
    copyButton.textContent = "Copied";
    setTimeout(() => {
        copyButton.textContent = "Copy JSON";
    }, 1200);
});
