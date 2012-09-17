// Build lookup table.
var LUT = new Object;
var letters = "abcdefghijklmnopqrstuvwxyz";
for (var i = 0; i < letters.length; ++i) {
  var c = letters[i];
  var c_rot13 = letters[(i + 13) % letters.length]
  LUT[c] = c_rot13;
  LUT[c.toUpperCase()] = c_rot13.toUpperCase();
}

// Return the rot13 translation of "text".
function rot13(text) {
  result = new Array;
  for (var i = 0; i < text.length; ++i) {
    var c = LUT[text[i]];
    // Use translation if available, original character otherwise.
    result.push(c ? c : text[i]);
  }
  return result.join('');
}

function updateTranslation() {
  document.inputForm.translationText.value =
      rot13(document.inputForm.sourceText.value);
}

// Focus and select the translation.
function selectTranslationText() {
  document.inputForm.translationText.focus();
  document.inputForm.translationText.select();
}

// Chrome security policy forbids inline event handlers, so register them here.
document.addEventListener('DOMContentLoaded', function () {
  document.inputForm.sourceText.addEventListener('change', updateTranslation);
  document.inputForm.sourceText.addEventListener('paste', updateTranslation);
  document.inputForm.sourceText.addEventListener('input', updateTranslation);
  document.inputForm.sourceText.addEventListener('blur', selectTranslationText);
});

window.onload = function(event) {
  // For some reason this doesn't seem to work on (at least) Windows 7.
  document.inputForm.sourceText.focus();
};
