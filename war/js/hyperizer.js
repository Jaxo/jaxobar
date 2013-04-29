var urlRegexp = new RegExp(
   "(?:" +
      "[a-z][\\w-]+:(?:\\/{1,3}|[a-z0-9%])" +
      "|" +
      "www\\d{0,3}[.]" +
      "|" +
      "[a-z0-9.\\-]+[.][a-z]{2,4}\\/" +
   ")" +
   "(?:" +
      "[^\\s()<>]+" +
      "|" +
      "\\((?:[^\\s()<>]+|(?:\\(?:[^\\s()<>]+\\)))*\\)" +
   ")+" +
   "(?:" +
      "\\((?:[^\\s()<>]+|(?:\\(?:[^\\s()<>]+\\)))*\\)" +
      "|" +
      "[^\\s`!()\\[\\]{};:'\".,<>?«»\u201c\u201d\u2018\u2019]" +
   ")",
   "mig"
);

function extractUrls(text) {
   var urls = [];
   while ((res = urlRegexp.exec(text)) !== null) {
      urls.push(parseURL(res[0]));
   }
   return urls;
}

function hypernavigate() {
   window.open(this.href, "JaxoBarLink");
}

function hyperactivate() {
   if (typeof MozActivity !== "undefined") {
      var a = new MozActivity(JSON.parse(this.href));
      a.onsuccess = function(e) {}
      a.onerror = function() {
         simpleMsg("error", i18n('pickImageError'));
      };
   }
}

function hyperize(input, elt) {
   var res;
   var start = 0;
   urlRegexp.lastIndex = 0;
   while ((res = urlRegexp.exec(input)) !== null) {
      if (start < res.index) {
         elt.appendChild(document.createTextNode(input.substring(start, res.index)));
      }
      var href = "";
      var text = res[0];
      var img;
      var anchorElt = document.createElement("SPAN");
      if (text.startsWith("sms:")) {
         href = (
            "{\"name\":\"new\",\"data\":{\"type\":\"websms/sms\",\"number\":\"" +
            text.substring(4) +
            "\"}}"
         );
         text = text.substring(4);
         img = "sms.png";
         anchorElt.onclick = hyperactivate;
      }else if (text.startsWith("tel:")) {
         href = (
            "{\"name\":\"dial\",\"data\":{\"number\":\"" +
            text.substring(4) +
            "\"}}"
         );
         text = text.substring(4);
         img = "dial.png";
         anchorElt.onclick = hyperactivate;
      }else {
         if (text.startsWith("mailto:")) {
            href = text;
            text = text.substring(7);
            img = "mailB.png";
         }else {
            if (text.startsWith("www")) {
               href = "http://" + text;
            }else if (text.startsWith("http://")) {
               href = text;
               text = text.substring(7);
            }else {
               href = text;
            }
            img = "earth.png";
         }
         anchorElt.onclick = hypernavigate;
      }
      var imgElt = document.createElement("IMG");
      imgElt.src = "style/images/" + img;
      anchorElt.appendChild(imgElt);
      anchorElt.appendChild(document.createTextNode(text));
      anchorElt.href = href;
      elt.appendChild(anchorElt);
      start = urlRegexp.lastIndex;
   }
   if (start < input.length) {
      elt.appendChild(document.createTextNode(input.substring(start, input.length)));
   }
}

function parseURL(url) {
   // var url = parseURL('http://abc.com:8080/dir/index.html?id=255&m=hello#top');
   // url.file;     // = 'index.html'
   // url.hash;     // = 'top'
   // url.host;     // = 'abc.com'
   // url.query;    // = '?id=255&m=hello'
   // url.params;   // = Object = { id: 255, m: hello }
   // url.path;     // = '/dir/index.html'
   // url.segments; // = Array = ['dir', 'index.html']
   // url.port;     // = '8080'
   // url.protocol; // = 'http'
   // url.source;   // = 'http://abc.com:8080/dir/index.html?id=255&m=hello#top'
   var a =  document.createElement('A');
   a.href = url;
   return {
      source: url,
      protocol: a.protocol.replace(':',''),
      host: a.hostname,
      port: a.port,
      query: a.search,
      params: (
         function() {
            var ret = {};
            var seg = a.search.replace(/^\?/,'').split('&');
            var len = seg.length;
            var s;
            for (var i=0; i<len; ++i) {
               if (!seg[i]) { continue; }
               s = seg[i].split('=');
               ret[s[0]] = s[1];
            }
            return ret;
         }
      )(),
      file: (a.pathname.match(/\/([^\/?#]+)$/i) || [,''])[1],
      hash: a.hash.replace('#',''),
      path: a.pathname.replace(/^([^\/])/,'/$1'),
      relative: (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [,''])[1],
      segments: a.pathname.replace(/^\//,'').split('/')
   };
}
