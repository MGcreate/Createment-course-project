window.onload = function () {
  let channelMenu = document.getElementById("channel_buttons");
  let sendButton = document.getElementById("sendButton");
  let messageField = document.getElementById("message_input");
  let newChannelName = document.getElementById("new_channel_name");
  let sortChannelMenu = document.getElementById("channel_sort_options");

  const checkChannelNameValidity = function() {
	  if(newChannelName.value.length > 20) {
		  newChannelName.setAttribute("readonly", "readonly");
		  newChannelName.value = newChannelName.value.slice(0, 20);
	   }
  }

  sortChannelMenu.onchange = sortChannels;

  newChannelName.onkeyup = checkChannelNameValidity;
  
  newChannelName.onclick = function() {
	  document.getElementById("channelAlreadyExistsMessage").style.display = "none";
      document.getElementById("channelNameError").style.display = "none";
  }

  
  //when someone pastes a text more than 20 characters with the mouse instead of keyboard
  newChannelName.onpaste = function(){
	  const myTimeout = setTimeout(checkChannelNameValidity, 2);
	}
	    
  
  newChannelName.onkeydown = function (e) {
	    if (e.key == "Backspace") {
	      newChannelName.removeAttribute("readonly");
	    }
	  };

  sendButton.onclick = SendMessage;
  messageField.onkeydown = function (e) {
    if (e.key == "Enter") {
      sendButton.onclick.apply();
    }
  };
  
  submitButtonChannel.onclick = SendChannel;

  fetch("/channels").then((inp) =>
    inp.json().then(function getChannels(channelArray) {
      for (channel of channelArray) {
        let channelP = channelToP(channel);

        channelMenu.appendChild(channelP);
      }
      LoadChannel(channelArray[0].name);
    })
  );
  
 setInterval(refreshChannel, 1000);
 setInterval(refreshChannelList, 5000);

 deleteButtonChannel.onclick = DeleteChannel;
};

function refreshChannelList(){
    let channelMenu = document.getElementById("channel_buttons");
    let dummy = document.createElement("div");
    fetch("/channels").then((inp) => {
        if (inp.ok) {
            inp.json().then(function getChannels(channelArray) {
                for (channel of channelArray) {
                    let channelP = channelToP(channel);
                    dummy.appendChild(channelP);
                }
                sortSelectedChannels(dummy);
                channelMenu.innerHTML = dummy.innerHTML;
            })
        }
        else {
            console.log("input niet ok");
        }
    });
}

function channelToP(channel){
    let channelP = document.createElement("p");
    channelP.setAttribute("name", channel.oid);
    channelP.setAttribute("id", channel.name);
    channelP.setAttribute("class", "channelButton");
    channelP.setAttribute("onclick", `LoadChannel("${channel.name}")`);
    channelP.append(channel.name);
    return(channelP);
}

function addMessage(message,target) {
  let messageDiv = messageToDiv(message);
          let messageDate = new Date(message.timestamp);
          if (target.firstChild != null) {
            let previousTimestampDiv = target.getElementsByClassName("messageTimestamp")[0];
            let previousTimestamp = new Date(previousTimestampDiv.getAttribute("title"));
            if (messageDate.setHours(0,0,0,0) != previousTimestamp.setHours(0,0,0,0)) {
              let newDayDiv = document.createElement("div");
              newDayDiv.setAttribute("class", "dayBreak");
              newDayDiv.append(messageDate.toDateString());
              target.prepend(newDayDiv);
            }
          } else {
            let newDayDiv = document.createElement("div");
            newDayDiv.setAttribute("class", "dayBreak");
            newDayDiv.append(messageDate.toDateString());
            target.prepend(newDayDiv);
          }
        target.prepend(messageDiv);
}

function LoadChannel(channelName) {
  let uri = "/channel/" + encodeURIComponent(channelName);
  let messageLog = document.getElementById("message_log");
  let channelTitle = document.getElementById("channel_name");

  let dummy = document.createElement("div");
  fetch(uri).then((inp) => {
	  if (inp.ok) {
	  inp.json().then(function getmessages(messageArray) {
	        for (let message of messageArray) {
	          addMessage(message, dummy);
	        }
	        messageLog.innerHTML = dummy.innerHTML;
	        channelTitle.innerHTML = channelName;
	        channelTitle.setAttribute("current_channel_name",channelName);
	  })} else {
          console.error("Channel doesn't exist");
          window.location= "/home";
      }
  }
    
  );
}

function refreshChannel() {
    let messageLog = document.getElementById("message_log");
    let channelname = document.getElementById("channel_name").getAttribute("current_channel_name");

    if (!messageLog.hasChildNodes()) {
        LoadChannel(channelname);
    } else {
        let lastChildId = messageLog.children[0].getAttribute("id");
        let lastOid = lastChildId.replace(/^(message-)/,'');
        let channelURI = encodeURI(channelname);
        let uri = `/channel/${channelURI}/message?last=${lastOid}`;

        fetch(uri).then((inp) => {
                if (inp.ok) {
                    inp.json().then(function getmessages(messageArray) {
                        for (let message of messageArray) {
                            if (document.getElementById("message-" + message.oid) == null) {
                                addMessage(message, messageLog);
                            }
                        }
                    })
                } else {
                    console.error("Channel doesn't exist");
                    window.location= "/home";
                }
            }
        )
    }


}

function messageToDiv(message){
    let messageDiv = document.createElement("div");
    let messageContentDiv = document.createElement("div");
    let messageUserDiv = document.createElement("div");
    let messageTimestampDiv = document.createElement("div");

    messageDiv.setAttribute("class", "singleMessage");
    messageDiv.setAttribute("id","message-" + message.oid);
    messageContentDiv.setAttribute("class", "messageContent");
    messageUserDiv.setAttribute("class", "messageUser");
    messageTimestampDiv.setAttribute("class", "messageTimestamp");
    messageTimestampDiv.setAttribute("title", message.timestamp);

    messageContentDiv.append(message.content);
    messageTimestampDiv.append(
        message.timestamp.substring(
            message.timestamp.length - 8,
            message.timestamp.length
        )
    );
    messageUserDiv.append(message.user.username);

    messageDiv.appendChild(messageTimestampDiv);
    messageDiv.appendChild(messageUserDiv);
    messageDiv.appendChild(messageContentDiv);

    return(messageDiv);
}

function SendMessage() {
  let input = document.getElementById("message_input").value;
  document.getElementById("message_input").value = "";
  let currentChannelName = document.getElementById("channel_name").innerHTML;

  input = input.trim();
  if (input) {
    let newMessageData = {
      content: input,
      channelName: currentChannelName,
    };

    let request = new XMLHttpRequest();
    request.open("POST", "/message");
    request.setRequestHeader("Content-Type", "application/json");
    request.onreadystatechange = function () {
      if (request.readyState == request.DONE) {
        refreshChannel(currentChannelName);
      }
    };
    request.send(JSON.stringify(newMessageData));
  }
}

function SendChannel() {
	  let input = document.getElementById("new_channel_name").value;
	  document.getElementById("new_channel_name").value = "";
        let letterNumber = /^[0-9a-zA-Z\s]{0,20}$/;
        if(!input.match(letterNumber)) {
            console.error("Channel name doesn't allow special characters");
            document.getElementById("channelNameError").style.display ="block";
        } else{
            if (input) {
                let newChannelData = {
                    name: input,
                };
                let request = new XMLHttpRequest();
                request.open("POST", "/channel");
                request.setRequestHeader("Content-Type", "application/json");
                request.onreadystatechange = function () {
                    if (request.readyState == request.DONE && request.status == 409) {
                        document.getElementById("channelAlreadyExistsMessage").style.display = "block";
                    } else {
                        refreshChannelList();
                        LoadChannel(input);
                    }
                };
                request.send(JSON.stringify(newChannelData));
            }
        }
}

function DeleteChannel(){

    let channelname = document.getElementById("channel_name").innerHTML;
    let request = new XMLHttpRequest();
    request.open("POST", "/delete-channel");
    request.setRequestHeader("Content-Type", "text/plain");
    request.onreadystatechange = function () {
        if (request.readyState == request.DONE) {
            window.location= "/home";
        }
    };
    request.send(JSON.stringify(channelname));
}

function sortChannels() {
  sortSelectedChannels(document.getElementById("channel_menu"));
}

function sortSelectedChannels(list) {
  let sortByFunctions = {
    sortAlphabetically: function(a,b) {
      return a.innerHTML.toLowerCase() > b.innerHTML.toLowerCase();
  },
    sortAlphabeticallyReverse: function(a,b) {
      return a.innerHTML.toLowerCase() < b.innerHTML.toLowerCase();
  },
    sortByOid: function(a,b) {
      return parseInt(a.getAttribute("name")) > parseInt(b.getAttribute("name"));
  },
    sortByOidReverse: function(a,b) {
      return parseInt(a.getAttribute("name")) < parseInt(b.getAttribute("name"));
  }
}
  let sortBy = document.getElementById("channel_sort_options").value;

  var list, i, switching, b, shouldSwitch;
  switching = true;
  while (switching) {
    switching = false;
    b = list.getElementsByTagName("p");
    for (i = 0; i < (b.length - 1); i++) {
      shouldSwitch = false;
      if (sortByFunctions[sortBy](b[i], b[i+1])) {
        shouldSwitch = true;
        break;
      }
    }
    if (shouldSwitch) {
      b[i].parentNode.insertBefore(b[i + 1], b[i]);
      switching = true;
    }
  }
}
