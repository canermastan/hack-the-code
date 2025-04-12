let editor;
let selectedLanguage;
let editorLanguage;

function unescapeHtml(html) {
    return html.replace(/&lt;/g, '<').replace(/&gt;/g, '>').replace(/&amp;/g, '&').replace(/&quot;/g, '\"').replace(/&#39;/g, '\'');
}

window.onload = () => {
    ace.require("ace/ext/language_tools");
    editor = ace.edit("editor");
    editor.setFontSize(17); // TODO make it dynamic
    let code = unescapeHtml(document.getElementById('code').innerHTML);
    editor.setValue(code, -1); // set value and move cursor to the start of the text
    editor.setTheme("ace/theme/eclipse"); // themes -> https://ace.c9.io/build/kitchen-sink.html
    editor.resize(true);
    editor.setOptions({
        enableBasicAutocompletion: true,
        enableSnippets: true,
        enableLiveAutocompletion: false,
        autoScrollEditorIntoView: true,
        copyWithEmptySelection: true,
    });
}

let languageName = null
$('#languages').dropdown().on("click", function () {
    editorLanguage = $('#languages').dropdown('get value');
    selectedLanguage = editorLanguage
    languageName = selectedLanguage
    switch (selectedLanguage) {
        case "go":
            editorLanguage = "golang";
            break;
        case "c" || "c++":
            editorLanguage = "c_cpp";
            break;
        case "c#":
            editorLanguage = "csharp";
            break;
    }
    editor.session.setMode("ace/mode/" + editorLanguage);
})

function getLanguageName() {
    return $('#languages').dropdown('get value');
}

function applyLanguage(taskId, languageName1) {
    let languageId = 0;
    switch (languageName) {
        case "python":
            languageId = 2;
            break;
        case "java":
            languageId = 3;
            break;
        case "c#":
            languageId = 4;
            break;
        case "c":
            languageId = 5;
            break;
        case "c++":
            languageId = 6;
            break;
        case "javascript":
            languageId = 7;
            break;
        case "go":
            languageId = 8;
            break;
        case "php":
            languageId = 9;
            break;
    }
    window.location.href = "/task/" + taskId + "?language=" + languageId
}

function executeCode(taskId, isForTest) {
    var $loading = $('#loading');

    $loading.show();

    $.ajax({
        type: "POST",
        url: "/compile",

        data: JSON.stringify({
            taskId: taskId,
            language: selectedLanguage,
            code: editor.getSession().getValue(),
            isForTest: isForTest
        }),
        contentType: "application/json",
        success: function (response) {
            var element = function () {

                return $('#output');
            };
            var $element = element()

            if (response.startsWith('{"timestamp"')) {
                $('#output').text('Sunucu hatası! Bu hata devam ederse lütfen bize ulaşın.');
                $element.css("color", "crimson")
                return;
            }
            $elementtus
                .text(response);
        },
        error: function (xhr) {

            var element = function () {
                return $('#output');
            };
            var $element = element()

            if (xhr.responseText.startsWith('{"timestamp"')) {
                $('#output').text('Sunucu hatası! Bu hata devam ederse lütfen bize ulaşın.');
                $element.css("color", "crimson")
                return;
            }

            if (xhr.responseText.search('GEÇTİNİZ!') !== -1) {
                $element.css("color", "green")
            } else {
                $element.css("color", "crimson")
            }
            $element.html(xhr.responseText);
        },
        complete: function () {
            $loading.hide();
        }
    })
}