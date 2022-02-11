function _id(name) {
  return document.getElementById(name);
}

function _class(name) {
  return document.getElementsByClassName(name)[0];
}

let visibleIcon = _id("visible");
let invisibleIcon = _id("invisible");
let usernameField = _id("username");
let pass1 = _id("passwordInput1");
let dd = _class("dropdown");
let userError = _id("username-error");
let passError = _id("pass-error");

usernameField.onchange = usernameExists;

function makeInvisible() {
  visibleIcon.style.display = "none";
  pass1.type = "text";
  invisibleIcon.style.display = "block";
}

function makevisible() {
  visibleIcon.style.display = "block";
  pass1.type = "password";
  invisibleIcon.style.display = "none";
}

function ValidateForm() {
  let b = usernameExists();
  return passwordInvalid() && b;
}

function usernameExists() {
  let username = usernameField.value;
  let url = "/user/exists/" + encodeURI(username);

  fetch(url).then((inp) =>
    inp.json().then(function checkuser(exists) {
      if (exists) {
        console.log("user bestaat");
        userError.style.display = "none";
        return true;
      } else {
        userError.style.display = "flex";
        console.log("user bestaat niet");
        return false;
      }
    })
  );
}

function passwordInvalid() {
  let reg = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&*$]).{8,20}$/;
  let pass = pass1.value;

  if (reg.exec(pass)) {
    passError.style.display = "none";
    return true;
  }
  passError.style.display = "flex";
  pass1.oninput = passwordInvalid;
  return false;
}
