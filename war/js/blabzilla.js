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
               document.getElementById('barImage').src = "images/unknown.png";
            }else {
               document.getElementById('barImage').src = (
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
      document.getElementById('barData').innerHTML = data;
      document.getElementById('barType').innerHTML = selectType.options[
         selectType.selectedIndex
      ].text;
      document.getElementById('barSize').innerHTML = selectSize.options[
         selectSize.selectedIndex
      ].text;
   }
}
