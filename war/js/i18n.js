var locale = "en-US";
var localeValues = {
   language: {
      'en-US':"Language: ",
      'fr-FR':"Langage : ",
      'pt-BR':"Língua: ",
      'es-ES':"Idioma: ",
      'pl-PL':"Język: "
   }, 'en-US': {
      'en-US':"US English",
      'fr-FR':"Anglais",
      'pt-BR':"Inglês",
      'es-ES':"Inglés",
      'pl-PL':"Angielski"
   }, 'fr-FR': {
      'en-US':"French",
      'fr-FR':"Français",
      'pt-BR':"Francês",
      'es-ES':"Francés",
      'pl-PL':"Francuski"
   }, 'pt-BR': {
      'en-US':"Portuguese",
      'fr-FR':"Portugais",
      'pt-BR':"Português",
      'es-ES':"Portugués",
      'pl-PL':"Portugalski"
   }, 'es-ES': {
      'en-US':"Spanish",
      'fr-FR':"Espagnol",
      'pt-BR':"Espanhol",
      'es-ES':"Español",
      'pl-PL':"Hiszpański"
   }, 'pl-PL': {
      'en-US':"Polish",
      'fr-FR':"Polonais",
      'pt-BR':"Polonês",
      'es-ES':"Polaco",
      'pl-PL':"Polski"
   }, months: {
      'en-US': [
          "January", "February", "March", "April", "May", "June",
          "July", "August", "September", "October", "November", "December"
      ],
      'fr-FR': [
          "janvier", "février", "mars", "avril", "mai", "juin",
          "juillet", "août", "septembre", "octobre", "novembre", "décembre"
      ],
      'pt-BR': [
          "janeiro", "fevereiro", "março", "abril", "maio", "junho",
          "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"
      ],
      'es-ES': [
          "enero", "febrero", "marzo", "abril", "mayo", "junio",
          "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
      ],
      'pl-PL': [
          "Styczeń", "Luto", "Marzec", "Kwiecień", "Maj", "Czerwiec",
          "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"
      ]
   }, date: {
      'en-US':"%2 %3, %1 at %4:%5",
      'fr-FR':"%3 %2 %1 à %4h%5",
      'pt-BR':"%3 de %2 de %1, às %4h %5",
      'es-ES':"%3 de %2 %1 a las %4h %5",
      'pl-PL':"%3 %2 %1 o %4:%5"
   }, btnInstall: {
      'en-US':"Install",
      'fr-FR':"Installer",
      'pt-BR':"Instalar",
      'es-ES':"Instalar",
      'pl-PL':"Zainstalować"
   }, betterInstall: {
      'en-US':"Jaxobar works nicer when installed,\nand it will remember your sign-in settings.\n\nWant to install now?",
      'fr-FR':"Jaxobar fonctionne mieux lorsqu'il est installé,\net il mémorise vos paramètres de connexion.\n\nVoulez-vous l'installer maintenant?",
      'pt-BR':"Jaxobar funciona melhor quando instalado\ne ele se lembra de suas configurações de conexão.\n\nDeseja instalar agora?",
      'es-ES':"Jaxobar funciona mejor cuando se instalan\ny se recuerda la configuración de conexión.\n\n¿Desea instalarlo ahora?",
      'pl-PL':"Jaxobar działa ładniejszy po zainstalowaniu\ni będzie pamiętać swój znak-w ustawieniach.\n\nZainstaluj go teraz?"
   }, installFailure: {
      'en-US':"Install has failed",
      'fr-FR':"Echec de l'installation",
      'pt-BR':"Instalação falhou",
      'es-ES':"Instalar ha fallado",
      'pl-PL':"Instalacja nie powiodła się"
   }, safariInstall: {
      'en-US':"To install, press the forward arrow in Safari and touch \"Add to Home Screen\"",
      'fr-FR':"Pour installer, appuyez sur la flèche droite dans Safari, puis \"Ajouter à l'écran d'accueil\"",
      'pt-BR':"Para instalar, pressione a seta para a frente no Safari e toque \"Adicionar à Tela Início\"",
      'es-ES':"Para instalarlo, pulse la flecha de avance en Safari y toca \"Añadir a pantalla de inicio\"",
      'pl-PL':"Aby zainstalować program, należy nacisnąć strzałkę do przodu w Safari i dotyku \"Dodaj do ekranu głównego\""
   }, OK: {
      'en-US':"OK",
      'fr-FR':"OK",
      'pt-BR':"OK",
      'es-ES':"Aceptar",
      'pl-PL':"Dobrze"
   }, cancel: {
      'en-US':"Cancel",
      'fr-FR':"Annuler",
      'pt-BR':"Cancelar",
      'es-ES':"Cancelar",
      'pl-PL':"Anulować"
   }, info: {
      'en-US':"Info",
      'fr-FR':"Information",
      'pt-BR':"Informação",
      'es-ES':"Info",
      'pl-PL':"Info"
   }, warning: {
      'en-US':"Warning",
      'fr-FR':"Alerte",
      'pt-BR':"Aviso",
      'es-ES':"Alerta",
      'pl-PL':"Ostrzeżenie"
   }, error: {
      'en-US':"Error",
      'fr-FR':"Erreur",
      'pt-BR':"Erro",
      'es-ES':"Error",
      'pl-PL':"Błąd"
   }, confirm: {
      'en-US':"Confirm",
      'fr-FR':"Validez",
      'pt-BR':"Confirmar",
      'es-ES':"Confirmar",
      'pl-PL':"Potwierdzać"
   }, testMode: {
      'en-US':"Test version.\nServer at\n%1",
      'fr-FR':"Version de test.\nServeur\n%1",
      'pt-BR':"A versão de teste.\nServidor\n%1",
      'es-ES':"Prueba de versión.\nServidor\n%1",
      'pl-PL':"Wersja testowa.\nServer na\n%1"
   }, noFileApi: {
      'en-US':"The file API isn't supported on this browser yet.",
      'fr-FR':"Pas d'interface 'File' pour ce navigateur",
      'pt-BR':"A API de arquivo não é compatível com este navegador ainda.",
      'es-ES':"La API de archivo no se admite en este navegador aún.",
      'pl-PL':"API pliku nie jest obsługiwany on tej przeglądarce jeszcze."
   }, noFileApiProp: {
      'en-US':"Your browser doesn't seem to support the 'files' property of file inputs.",
      'fr-FR':"L'interface 'File' n'a pas la propriété 'files'",
      'pt-BR':"Seu navegador não parecem apoiar a propriedade dos 'arquivos' de entradas de arquivos.",
      'es-ES':"Su navegador no parecen apoyar la propiedad de los 'archivos' de las entradas de archivo.",
      'pl-PL':"Twoja przeglądarka nie wydają się potwierdzać własność 'pliki' wejść plików."
   }, noFileSelected: {
      'en-US':"No file selected",
      'fr-FR':"Pas de fichier selecté",
      'pt-BR':"Nenhum arquivo selecionado",
      'es-ES':"No existe el fichero seleccionado",
      'pl-PL':"Nie wybrany plik"
   }, pickImageError: {
      'en-US':"The selection of the photo has failed",
      'fr-FR':"Erreur lors de la sélection de la photo",
      'pt-BR':"Falha seleccionando uma foto",
      'es-ES':"La falta seleccionar una foto",
      'pl-PL':"Wybór zdjęcie nie udało"
   }, p0_top: {
      'en-US':"Encode and decode most usual barcodes",
      'fr-FR':"Encodez et décodez la plupart des codes à barres",
      'pt-BR':"Codificar e decodificar códigos de barras mais comuns",
      'es-ES':"Codificar y decodificar códigos de barras más usuales"
   }, btnReveal: {
      'en-US':"Reveal",
      'fr-FR':"Dévoiler",
      'pt-BR':"Revelar",
      'es-ES':"Revelar"
   }, btnEdit: {
      'en-US':"Edit",
      'fr-FR':"Editer",
      'pt-BR':"Editar",
      'es-ES':"Editar"
   }, barDataIn: {
      'en-US':"Enter your text…",
      'fr-FR':"Entrez votre texte…",
      'pt-BR':"Digite seu texto…",
      'es-ES':"Introduzca el texto…"
   }, decodeFrom: {
      'en-US':"Decode from:",
      'fr-FR':"Décoder : ",
      'pt-BR':"Decodificar:",
      'es-ES':"Descodificar:"
   }, encodeIn: {
      'en-US':"Encode in:",
      'fr-FR':"Encodage : ",
      'pt-BR':"Codificar em:",
      'es-ES':"Codificar en:"
   }, footEncode: {
      'en-US':"Encode",
      'fr-FR':"Encoder",
      'pt-BR':"Codificar",
      'es-ES':"Codificar"
   }, footDecode: {
      'en-US':"Decode",
      'fr-FR':"Décoder",
      'pt-BR':"Decodificar",
      'es-ES':"Descodificar"
   }, unspecified: {
      'en-US':"[unspecified]",
      'fr-FR':"[non spécifié]",
      'pt-BR':"[indeterminado]",
      'es-ES':"[sin especificar]"
   }, noInfos: {
      'en-US':"[no infos available]",
      'fr-FR':"[aucune information disponible]",
      'pt-BR':"[não há informações disponíveis]",
      'es-ES':"[no hay información disponible]"
   }
}
/*----------------- end of strings requiring translation --------------------*/
function i18n(msgName) {
   var value;
   var values = localeValues[msgName];
   if (values === undefined) {
      value = "unknown text: " + msgName;
   }else {
      value = values[locale];
      if (value === undefined) {
         value = values['en-US'];
      }
   }
   for (var i=1, max=arguments.length; i < max; ++i) {
      value = value.replace("%"+i, arguments[i]);
   }
   return value;
}
function translateBody(newLocale) {
   locale = newLocale;
   var elts = document.getElementsByClassName('i18n');
   loop: for (var i=0, iMax=elts.length; i < iMax; ++i) {
      var elt = elts[i];
      if (elt.className !== "i18n") {
         /*
         | Following allows to modify an I18n attribute value
         | rather than (or in conjunction with) the text content
         */
         var classNames = elt.className.split(/\s/);
         for (var j=0, jMax = classNames.length; j < jMax; ++j) {
            var className = classNames[j];
            if (className.startsWith("i18n") && (className.length > 4)) {
               // assume: className.substring(4) is PH (placeholder)
               elt.setAttribute("placeholder", i18n(elt.id));
               continue loop; // forget about the text content
            }
         }
      }
      elt.textContent = i18n(elt.id);
   }
}
function i18nDate(time) {
   var date = new Date(+time);
// date = new Date(1366018659077);
   return i18n(
      "date",
      date.getFullYear(),
      (i18n("months"))[date.getMonth()],
      date.getDate(),
      date.getHours(),
      date.getMinutes()
   );
}
