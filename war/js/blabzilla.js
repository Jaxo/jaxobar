var isPackaged = true;
var server_url = "http://blabzilla.appspot.com";
// -- only for our internal testing --
// var server_url = "http://5.blabzilla.appspot.com";
// var server_url = "http://localhost:8888";

var encodeInfos = "";
var decodeInfos = "";
var actualBarcodeData = null;
var actualBarcodeType = null;
var actualBarcodeOptions = "";
var actualBarcodeSize = "";

window.onload = function() {
   var loc = window.location;
   if (loc.protocol !== "app:") {
      isPackaged = false;
      var host = loc.host;
      if (host.indexOf("appspot") >= 0) {     // appspot default, or versioned
         server_url = "http://" + host;
      }else {                                 // "http://localhost:8888", or
         server_url = "http://localhost:8888" // "http://ottokar/jaxogram/index.html"
      }
   }
   //OT-LH*/ isPackaged = true;  // testing on ottokar/localhost
   if (server_url !== "http://blabzilla.appspot.com") {
      alert("Warning: test version\nServer at\n" + server_url);
   }
   createDispatcher();
   setInterval("encodeIfNeeded()", 60);
   setInstallButton("btnInstall");
   document.getElementById('footEncode').click();
   fitImage(document.getElementById('barImageOut'));
   window.addEventListener("resize", fitImages, false);
   dispatcher.on(
      "install_changed",
      function action(state, version) {
         if ((state === "installed") && version) {
            document.querySelector("header h1 small").textContent = version;
         }
      }
   );
   document.getElementById("p1").addEventListener(
      'transitionend',
      function(event) {
         p1Expanded(event.target.attributes["aria-expanded"].value == "true");
//       alert(
//          "Finished transition! " + event.target.id +
//          " " + event.target.attributes["aria-expanded"].value +
//          " " + document.getElementById("p1").style.zIndex +
//          " " + event.propertyName
//       );
      },
      false
   );
   // Listeners
   document.getElementById("btnMain").onclick = toggleSidebarView;
   document.getElementById("btnInfos").onclick=showInfos;
   document.getElementById("btnReveal").onclick=revealOrNot;
   document.getElementById("sidebarMenu").onclick = menuListClicked;
   document.getElementById("footMenuList").onclick = menuListClicked;
   document.getElementById("encodeTypeList").onclick=setEncName;
// document.getElementById("jgUsersAid").onclick = listAlbums;
// document.getElementById("changeLanguage").onclick = changeLanguage;
   document.getElementById("footerTable").onclick = function() { expandSidebarView(-1); };
   document.getElementById("footDecode").onclick = pickAndUpload;
// document.getElementById("pickAndUpload").onclick = pickAndUpload;
// document.getElementById("whoAmI").onclick = whoAmI;

   var eltMain = document.getElementById("main");
   new GestureDetector(eltMain).startDetecting();
   eltMain.addEventListener("swipe", swipeHandler);
}

function p1Expanded(isExpanded) {
   var style = document.getElementById("btnReveal").style;
   if (isExpanded) {
      style.display ="";
   }else {
      style.display = "none";
   }
}

function swipeHandler(e) {
   var direction = e.detail.direction;
   if (direction === 'right') {
      expandSidebarView(1);
   }else if (direction === 'left') {
      expandSidebarView(-1);
   }
}

function fitImages(img) {
   // workaround, until "object-fit:contain;" gets implemented
   fitImage(document.getElementById('barImageIn'));
   fitImage(document.getElementById('barImageOut'));
}
function fitImage(img) {
   var elt = document.getElementById("p1");
   if ((elt.offsetHeight * img.offsetWidth)<(img.offsetHeight * elt.offsetWidth)) {
      s = "height:100%";
   }else {
      s = "width:100%";
   }
   img.setAttribute("style", s);
}

function makeCorsRequest(method, query) {
   var xhr = new XMLHttpRequest({mozSystem: true});
   if (xhr.withCredentials === undefined) {
      alert("Sorry: your browser doesn't handle cross-site requests");
      return;
   }
   xhr.withCredentials = true;
   xhr.open(method, server_url + query, true);
   return xhr;
}

function encodeIfNeeded() {
   var data = document.getElementById("barDataIn").value;
   var size = "M";
   var type = getEncodeType();
   var options = "";
// var mods = eval("document.encodeForm.MOD_" + type);
// if (typeof mods !== "undefined") {
//    var mod = "";
//    if (mods.nodeName == "SELECT") {
//       mod +=  mods.options[mods.selectedIndex].value + ";";
//    }else {
//       for (var i=0; i < mods.length; ++i) {
//          v = mods[i];
//          mod +=  v.options[v.selectedIndex].value + ";";
//       }
//    }
//    options = "&MOD=" + escape(mod);
// }
   if (
      (data != actualBarcodeData) ||
      (type != actualBarcodeType) ||
      (size != actualBarcodeSize) ||
      (options != actualBarcodeOptions)
   ) {
      actualBarcodeData = data;
      actualBarcodeType = type;
      actualBarcodeSize = size;
      actualBarcodeOptions = options;

      var xhr = makeCorsRequest(
         "GET",
         "/encode?KEY=" + escape(data) +
         "&TYP=" + escape(type) +
         "&SIZ=" + escape(size) +
         "&B64=1" + options
      );
      xhr.onreadystatechange = function() {
         // if (this.readyState == 2) { // HEADERS_RECEIVED is 2
         //   document.getElementById('headers').innerHTML = this.getAllResponseHeaders();
         // }
         if (this.readyState == 4) {    // DONE is 4
            encodeInfos = this.getResponseHeader("Jaxo-Infos");
            if (this.status != 200) {
               document.getElementById('barImageOut').src = "images/unknown.png";
            }else {
               document.getElementById('barImageOut').src = (
                  "data:image/bmp;base64," + this.responseText
               );
            }
         }
      }
      xhr.send();
/*
      document.getElementById('barData').innerHTML = data;
      document.getElementById('barType').innerHTML = selectType.options[
         selectType.selectedIndex
      ].text;
      document.getElementById('barSize').innerHTML = selectSize.options[
         selectSize.selectedIndex
      ].text;
*/
   }
}

function pickAndUpload(event)
{
   try {
      uploadPick();
   }catch (err) {
      uploadFile();
   }
}

function uploadPick() {
   var a = new MozActivity({ name: "pick", data: {type: "image/jpeg"}});
   a.onsuccess = function(e) {
      var xhr = makeCorsRequest("POST", "/decodeImage?TYP="+collectDecodeTypes()+"&PPC=" + getPostProcess());
      xhr.setRequestHeader("Content-Type", 'image/jpeg');
      xhr.onreadystatechange = whenRequestStateChanged;
      xhr.send(a.result.blob);
      try {
         var url = URL.createObjectURL(a.result.blob);
         var img = document.getElementById('barImageIn');
         img.src = url;
         img.onload = function() { URL.revokeObjectURL(url); };
      }catch (error) {
         alert("Local error: " + error);
      }
   };
   a.onerror = function() { alert('Failure at picking an image'); };
}

function uploadFile() {
   var elt = document.getElementById('upldFile');
   elt.onchange = function() {
      if (typeof window.FileReader !== 'function') {
         alert("The file API isn't supported on this browser yet.");
      }else if (!this.files) {
         alert("Your browser doesn't seem to support the `files` property of file inputs.");
      }else if (!this.files[0]) {
         alert("No file selected");
      }else {
         var file = this.files[0];
         var formData = new FormData();
         formData.append("MAX_FILE_SIZE", "1000000");
         formData.append("TYP", collectDecodeTypes());
         formData.append("PPC", getPostProcess());

         formData.append("upldFile", file);
         var xhr = makeCorsRequest("POST", "/decodeFile");
         xhr.onreadystatechange = whenRequestStateChanged;
         xhr.send(formData);
         var reader = new FileReader();
         reader.onload = function (event) {
            document.getElementById("barImageIn").src = event.target.result;
         };
         reader.readAsDataURL(file);
      }
   }
   elt.click();
}

function whenRequestStateChanged() {
   switch (this.readyState) {
   case 1: // OPENED
      document.getElementById("progresspane").style.visibility='visible';
//    document.getElementById('imgSource').innerHTML = "";
      decodeInfos = "";
      document.getElementById('barDataOut').innerHTML = "";
//    document.getElementById('imgBarType').innerHTML = "";
      break;
   case 4: // DONE
      document.getElementById("progresspane").style.visibility='hidden';
//    document.getElementById('imgSource').innerHTML = this.source;
      decodeInfos = this.getResponseHeader("Jaxo-Infos");
      if (this.status == 200) {
         document.getElementById('barDataOut').innerHTML = this.responseText;
//       document.getElementById('imgBarType').innerHTML = this.getResponseHeader("Jaxo-Symbo");
//       // show PostProcess result, if any
//       var upcImg = this.getResponseHeader("Jaxo-UpcImg");
//       if (upcImg != null) {
//          var descr;
//          var upcDescr = this.getResponseHeader("Jaxo-UpcDescr");
//          if (upcDescr == null) {
//             descr = "";
//          }else {
//             descr = "description=" + escape(upcDescr) + "&";
//          }
//          document.getElementById('imgToPin').src = upcImg;
//          document.getElementById('pinterestAnchor').href=(
//             "http://pinterest.com/pin/create/button/?" + descr +
//             "url=www.jaxo-systems.com/pinterest" +
//             "&media=" + upcImg
//          );
//          document.getElementById('pinned').style.visibility='visible';
//       }
      }else {
         alert(
            "Error " + this.status + " " + this.statusText +
            "\n" + decodeInfos
         );
      }
      break;
   }
}

function getEncodeType() {
   var children = document.getElementById("encodeTypeList").children;
   for (var i=0; i < children.length; ++i) {
      var child = children[i];
      if (child.getAttribute("aria-selected") == "true") {
         return child.getAttribute("aria-label");
      }
   }
   return "1";
}

function setEncName(event) {
   document.getElementById('encName').innerHTML = event.target.innerHTML;
   this.parentNode.click();
}

function collectDecodeTypes() {
   var children = document.getElementById("decodeTypeList").children;
   var types = "";
   for (var i=0; i < children.length; ++i) {
      var child = children[i];
      if (child.getAttribute("aria-selected") == "true") {
         if (types.length > 0) types += ",";
         types += child.getAttribute("aria-label");
      }
   }
   if (types.lenth == 0) types = "0";
   return types;
}

function getPostProcess() {
   // <INPUT type="hidden" name="PPC" value="P0"/> <!-- Postprocess: none -->
   return "P0";
}

function revealOrNot() {
   var style = document.getElementById('boxImageOut').style;
   if (style.zIndex == 2) {    // Reveal -> Edit
      document.getElementById("btnReveal").innerHTML = "Reveal";
      style.zIndex = 0;
   }else {                     // Edit -> Reveal
      document.getElementById("btnReveal").innerHTML = "Edit";
      style.zIndex = 2;
   }
}

function showInfos() {
   var isEncode = (
      document.getElementById('p1').getAttribute("aria-expanded") === "true"
   );
   if (isEncode && (encodeInfos) && (encodeInfos !== "")) {
      alert(encodeInfos);
   }else if (!isEncode && (decodeInfos) && (decodeInfos !== "")) {
      alert(decodeInfos);
   }else {
      alert("No infos available");
   }
}

