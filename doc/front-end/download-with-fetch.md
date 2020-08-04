```javascript
const options = {
  headers: {
    Authorization: `Bearer ${token}}`,
  }
};

export const download = (href) => {
    fetch(href, options)
        .then(async res => {
                if (!res.ok) {
                    const json = await res.json();
                    message.error(json.message);
                    return;
                }
                let filename = '';
                const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                const matches = filenameRegex.exec(decodeURIComponent(res.headers.get('content-disposition')));
                if (matches != null && matches[1]) {
                    filename = matches[1].replace(/['"]/g, '');
                }

                const blob = new Blob([await res.blob()], {type: res.headers.get('content-type')});
                if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                    window.navigator.msSaveOrOpenBlob(blob);
                } else {
                    const url = (window.URL ? URL : webkitURL).createObjectURL(blob);

                    const a = document.createElement("a");
                    a.href = url;
                    a.download = filename;
                    a.click();
                    setTimeout(() => {
                        window.URL.revokeObjectURL(url);
                    }, 200);
                    a.remove();
                }
            }
        );
};
```
