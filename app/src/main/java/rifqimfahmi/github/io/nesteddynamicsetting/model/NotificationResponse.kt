package rifqimfahmi.github.io.nesteddynamicsetting.model

interface Visitable {

}

class NotificationResponse(
    var email: List<EmailSectionSetting>
)

class EmailSectionSetting(
    var section: String,
    var settings: List<Setting>
): Visitable

class Setting(
    var name: String,
    var icon: String,
    var key: String,
    var status: Boolean,
    var description: String,
    var childSettings: List<Setting>
) : Visitable {

    fun hasChildSetting(): Boolean {
        return childSettings.isNotEmpty()
    }
}

object NotificationSettingHelper {

    fun createDummyResponse(): ArrayList<Visitable> {
        val transactionSettings = listOf(
            Setting("Transaksi Pembelian", "", "1", true, "", emptyList())
        )

        val chatSetting = listOf(
            Setting("Dari Tokopedia", "", "1", true, "", emptyList()),
            Setting("Chat Promosi dari Penjual", "", "1", true, "", emptyList()),
            Setting("Chat Penjual", "", "1", true, "", emptyList())
        )
        val inboxSettings = listOf(
            Setting("Chat", "", "1", true, "", chatSetting),
            Setting("Diskusi", "", "1", true, "", emptyList()),
            Setting("Ulasan", "", "1", true, "", emptyList())
        )

        val updateSettings = listOf(
            Setting("Aktivitas", "", "1", true, "Reminder, Feeds, dan Aktivitas akun kamu", emptyList()),
            Setting("Promo", "", "1", true, "", emptyList()),
            Setting("Info", "", "1", true, "Informasi terbaru seputar Tokopedia", emptyList())
        )

        val emailSectionSettings = listOf(
            EmailSectionSetting(
                "Transaksi",
                transactionSettings
            ),
            EmailSectionSetting(
                "Inbox",
                inboxSettings
            ),
            EmailSectionSetting(
                "Update",
                updateSettings
            )
        )

        val flatten = arrayListOf<Visitable>()

        for (emailSectionSetting in emailSectionSettings) {
            flatten.add(emailSectionSetting)
            for (setting in emailSectionSetting.settings) {
                flatten.add(setting)
            }
        }

        return flatten
    }
}