package com.example.mtjobs.ui.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import com.example.mtjobs.R
import com.example.mtjobs.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment(R.layout.fragment_details) {
    private lateinit var binding: FragmentDetailsBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        binding.detailsJobDescription.text = Html.fromHtml(
            "<p>2002 sind wir als Internetagentur gestartet. Heute bezeichnen wir uns als Digitalagentur. Das Spielfeld ist heute breiter und greift tiefer in bestehende Geschäftsbereiche ein. In unseren 3 Units MARKETING, DIGITAL &amp; TALENT arbeiten über 80 Macher, Nerds und Kreative nach einem Prinzip: Messbar mehr Erfolg. Das heißt konkret: Klare Ziele. Permanente Optimierung. Transparentes Reporting.</p>\n<p><strong>Wer wir sind</strong></p>\n<p>2002 sind wir als Agentur gestartet. Heute arbeiten über 80 Macher, Nerds und Kreative bei uns. Wir sind Strategen, Experten, Zuhörer, Kollegen, Freunde, Aufdenpunktbringer, Sprachgewandte, Texter und Erfolgshungrige.\nGemeinsam entwickeln wir sehr wirkungsvolle Lösungen in den Bereichen Digitales Marketing, Employer Branding und Digitalisierung für Marken, wie Cinestar, Vodafone, Garmin, Wemag und viele mehr. Wir entwickeln Online-Marketing-Strategien, bauen innovative Websites und haben auch sonst die gesamte Vielfalt des Agenturlebens zu bieten.</p>\n<p><strong>Das wären Deine Aufgaben</strong></p>\n<p><strong>Du willst eine Mandarine werden?</strong>\nDu liebst gut bedienbare, gut aussehende und gut funktionierende Websites?\nDu liebst es noch mehr, diese selbst zu entwickeln?\nDu möchtest mit einem erfahrenen Team aus Anwendungsentwicklern, Konzeptern, Designern, Marketingexperten uvm. arbeiten? Eine Crew hinter dir haben, dir dir das Frontend überlässt?\nDann bist du die perfekte Mandarine im Bereich FRONTEND DEVELOPMENT!</p>\n<p><strong>Das bringst Du mit</strong></p>\n<p><strong>DAS SOLLTEST DU MITBRINGEN:</strong></p>\n<ul>\n<li>Fundierte Kenntnisse in HTML,CSS, Javascript</li>\n<li>Umgang mit entsprechenden Techniken und Frameworks (React, LESS/SASS, Tailwind, Bootstrap o.ä.)</li>\n<li>Eigenständiges Arbeiten, Teamgeist, Kommunikationsfähigkeit</li>\n<li>Ein sehr hohes Qualitätsbewusstsein</li>\n</ul>\n<p><strong>Von Vorteil sind:</strong></p>\n<ul>\n<li>Grundlegende Programmierkenntnisse z.B. in PHP</li>\n<li>Verständnis von OOP</li>\n<li>GIT Kenntnisse</li>\n<li>Verständnis von Datenbanken, z.B. MySQL</li>\n<li>Verständnis von API</li>\n<li>Alltagssicheres Englisch</li>\n</ul>\n<p><strong>Nahezu großartig wären:</strong></p>\n<ul>\n<li>Drupal 7&amp;8 Kenntnisse</li>\n<li>WordPress Erfahrung</li>\n<li>Verständnis von Template Engines wie Twig oder Blade</li>\n</ul>\n<p>Klingt ganz schön viel? Nobody is perfect! Wir lernen jeden Tag voneinander und nehmen uns jede Woche Zeit für Weiterbildung. Die ein oder andere Fähigkeit kannst Du also auch nach Deinem Start bei uns erlernen oder vertiefen.</p>\n<p><strong>Das bieten wir Dir</strong></p>\n<p><strong>DAS KÖNNEN WIR DIR BIETEN:</strong></p>\n<ul>\n<li>Einen spannenden und anspruchsvollen Agentur-Job in MV</li>\n<li>Eine große Bandbreite an Projekten - Handwerk bis Konzern</li>\n<li>Ein hervorragendes Arbeitsklima und kurze Entscheidungswege</li>\n<li>4 Stunden Zeit für deine Weiterbildung je Woche plus Budget</li>\n<li>Neueste Arbeitsmittel und Programme</li>\n<li>Unterstützung deiner Gesundheit</li>\n<li>Fahrtkostenzuschuss</li>\n<li>Flexible Arbeitsorte</li>\n<li>Kreative Denkpausen mit dem Boot auf dem Schweriner See</li>\n<li>Teamevents, wie z.B. Grillen, MANDARIN-Kino, Besuch des Weihnachtsmarktes und noch ein paar mehr</li>\n<li>Ein Büro mit Blick auf den Schweriner See sowie lecker Obst, Kaffee, Tee, Wasser und Mate</li>\n</ul>\n<p><strong>Kontakt</strong></p>\n<p>Passt alles für Dich? Dann schick deine Bewerbung mit deinen Vorstellungen zu Gehalt und Urlaub an Corinna Lepsien, <a href=\"mailto:jobs@mandarin-medien.de\">jobs@mandarin-medien.de</a>. Oder falls du gerade unterwegs bist einfach einen Link zu deinem Xing- oder LinkedIn Profil.</p>\n<p><img src=\"https://camo.githubusercontent.com/da6ee29a15ea655c323d5f0c1b730031e993f7a6/68747470733a2f2f742e676f686972696e672e636f6d2f682f37306233363363363563343163653431666337333065333861633565626333313838333133653163613361323831303233333738376431333236313139303537\"></p>\n",
            Html.FROM_HTML_SEPARATOR_LINE_BREAK_HEADING
        )
    }
}