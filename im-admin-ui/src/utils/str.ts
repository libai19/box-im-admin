export const html2Escape = (strText) => {
	return strText.replace(/[<>&"]/g, function(c) {
		return {
			'<': '&lt;',
			'>': '&gt;',
			'&': '&amp;',
			'"': '&quot;'
		} [c];
	});
}