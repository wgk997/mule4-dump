%dw 2.0
output application/json
---
payload map ( song ) -> {
	songId: song.SONG_ID default 0,
	songTitle: song.SONG_TITLE default "",
	genre: song.GENRE default "",
	publicationDate: song.PUBLICATION_DT as String {format: "yyyy-MM-dd"},
	duration: song.DURATION default "",
	artistName: song.ARTIST_NM default "",
	albumId: song.ALBUM_ID default 0,
	albumName: song.ALBUM_NM default ""
}
