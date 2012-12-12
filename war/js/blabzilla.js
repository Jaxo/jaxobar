var actualBarcodeData = null;
var actualBarcodeType = null;
var actualBarcodeOptions = "";
var actualBarcodeSize = "";

function encodeIfNeeded() {
   var data = document.encodeForm.KEY.value;
   var selectSize = document.encodeForm.SIZ;
   var size = selectSize.value;
   var selectType = document.encodeForm.TYP;
   var type = selectType.value;
   var mods = eval("document.encodeForm.MOD_" + type);
   var options = "";
   if (typeof mods !== "undefined") {
      var mod = "";
      if (mods.nodeName == "SELECT") {
         mod +=  mods.options[mods.selectedIndex].value + ";";
      }else {
         for (var i=0; i < mods.length; ++i) {
            v = mods[i];
            mod +=  v.options[v.selectedIndex].value + ";";
         }
      }
      options = "&MOD=" + escape(mod);
   }
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
      // see: http://www.w3.org/TR/XMLHttpRequest/
      request = new XMLHttpRequest();
      request.onreadystatechange = function() {
         // if (this.readyState == 2) { // HEADERS_RECEIVED is 2
         //   document.getElementById('headers').innerHTML = this.getAllResponseHeaders();
         // }
         if (this.readyState == 4) {    // DONE is 4
            // document.getElementById('barInfos').innerHTML = this.getResponseHeader("Jaxo-Infos");
            if (this.status != 200) {
               document.getElementById('barImageOut').src = "images/unknown.png";
            }else {
               document.getElementById('barImageOut').src = (
                  "data:image/bmp;base64," + this.responseText
               );
            }
         }
      }
      request.open(
         "GET",
         "encode?KEY=" + escape(data) +
         "&TYP=" + escape(type) +
         "&SIZ=" + escape(size) +
         "&B64=1" +
         options
      );
      request.send();
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

function pickAndDecodeImage()
{
   var a = new MozActivity({ name: "pick", data: {type: "image/jpeg"}});
   a.onsuccess = function(e) {
     var request = new XMLHttpRequest();
     request.open(
        "POST",
        "decodeImage" +
        "?TYP=" + document.decodeForm.TYP.value +
        "&PPC=" + document.decodeForm.PPC.value,
        true
     );
     request.setRequestHeader("Content-Type", 'image/jpeg');
     request.onreadystatechange = onImageDecoded;
     request.send(a.result.blob);
     var url = URL.createObjectURL(a.result.blob);
     var img = document.getElementById('barImageIn');
     img.src = url;
     img.onload = function() { URL.revokeObjectURL(url); };
   };
   a.onerror = function() { alert('Failure picking photo'); };
}

function onImageDecoded() {
   switch (this.readyState) {
// case 1: // OPENED
//    document.getElementById("loading").style.visibility='visible';
//    document.getElementById('imgSource').innerHTML = "";
//    document.getElementById('imgInfos').innerHTML = "";
//    document.getElementById('imgContents').innerHTML = "";
//    document.getElementById('imgBarType').innerHTML = "";
//    break;
   case 4: // DONE
//    document.getElementById("loading").style.visibility='hidden';
//    document.getElementById('imgSource').innerHTML = this.source;
//    document.getElementById('imgInfos').innerHTML = this.getResponseHeader("Jaxo-Infos");
      if (this.status == 200) {
         document.getElementById('imgContents').innerHTML = this.responseText;
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
      }
      break;
   }
}
