// ##### PROTEIN SCAN #####

// PROTEIN IDENTIFIER CHECK BOX
$('#multipleProteinIdentifierCheckBox').change(function() {
    if ($(this).is(':checked')) {
        document.getElementById("singleProteinIdentifierInput").disabled = true;
        document.getElementById("multipleProteinIdentifierInput").disabled = false;
    } else {
        document.getElementById("singleProteinIdentifierInput").disabled = false;
        document.getElementById("multipleProteinIdentifierInput").disabled = true;
    }
});


// PROTEIN SCAN ADD ENTRY BUTTON
var selectedTab = document.getElementById('menu1'),
    addedEntries = [];

$('#addSourceEntryButton').click(function () {
   var entryData = "entry:{",
       processedIdentifiers = [];

   if ($('#menu2Selector').attr('aria-expanded') === "true") {
       debugger;
       entryData += "type:sequence, ";
       var proteinSequenceName = document.getElementById('proteinSequenceName'),
           proteinScanSequenceTextArea = document.getElementById('proteinScanSequenceTextArea'),
           sequenceName = proteinSequenceName.value,
           proteinSequence = proteinScanSequenceTextArea.value;

       if (!sequenceName) {
           sequenceName = "Custom Protein"
       }
       if (!proteinSequence) {
           alert("Missing protein sequence!");
           return;
       } else {
           proteinSequenceName.value = "";
           proteinScanSequenceTextArea.value = "";
       }
       entryData += "name:" + sequenceName.toString() + ", " + "seq:" + proteinSequence;

   } else if ($('#menu3Selector').attr('aria-expanded') === "true") {
       selectedTab = document.getElementById('menu3');
       entryData += "type:file, ";

       // TODO!!!

   } else {
       // if ($('#menu1Selector').attr('aria-expanded') === "true")
       entryData += "type:identifiers, dataSource:" + $('#proteinDataSourceSelect').find(':selected').text() + ", ";
       if (!document.getElementById('multipleProteinIdentifierCheckBox').checked) {
           var singleProteinIdentifierInput = document.getElementById('singleProteinIdentifierInput');
           processedIdentifiers = singleProteinIdentifierInput.value;
           singleProteinIdentifierInput.value = "";
       } else {
           var multipleProteinIdentifierInput = document.getElementById('multipleProteinIdentifierInput');
           var identifiers = multipleProteinIdentifierInput.value.split('\n');
           multipleProteinIdentifierInput.value = "";
           $.each(identifiers, function (i, item) {
               var buffer = item.split(',');
               $.each(buffer, function (j, subitem) {
                   processedIdentifiers.push(subitem.trim());
               });
           });
       }
       if (!processedIdentifiers) {
           console.log("Missing identifiers");
           return;
       } else {
           console.log("Identifiers: " + processedIdentifiers.toString());
           entryData += "identifiers:[" + processedIdentifiers.toString() + "]";
       }
       // addedEntries.append()

   }


   entryData += "}";

   addedEntries.push(entryData);
   updateAddedEntriesContent();

   // alert(selectedTab.innerHTML);
});


function updateAddedEntriesContent() {
    var entriesHtml = "",
        entryHtml;
   $.each(addedEntries, function (i, item) {
      entryHtml = '<div id="addedEntries" class="row select-entry">'
          +'<input class="form-control proteinScanRemoveEntryButton" type="text" size="80px" readonly value="'
          + item + '">'
          + '&nbsp;<button onclick="removeButtonAction(this.name)" name="' + i +'" class="btn btn-default" type="button">Remove</button> '
          + '</div>';
      entriesHtml += entryHtml;
   });

    $('#addedEntries').html(entriesHtml + "<br/>");
}


function removeButtonAction(name) {
    console.log(name);
    var buffer = [];
    for (var i = 0; i < addedEntries.length; i += 1) {
        if (name.toString() !== i.toString()) {
            buffer.push(addedEntries[i]);
        }
    }
    addedEntries = buffer;
    updateAddedEntriesContent();
}