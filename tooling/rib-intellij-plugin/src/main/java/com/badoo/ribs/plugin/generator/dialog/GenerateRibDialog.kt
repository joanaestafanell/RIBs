package com.badoo.ribs.plugin.generator.dialog

import com.badoo.ribs.plugin.template.Template
import com.badoo.ribs.plugin.template.Token
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL
import com.intellij.uiDesigner.core.GridConstraints.FILL_NONE
import com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED
import com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.uiDesigner.core.Spacer
import java.awt.Dimension
import javax.swing.JComboBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingUtilities.invokeLater


class GenerateRibDialog(private val listener: Listener,
                        templates: List<Template>) : DialogWrapper(null) {

    interface Listener {
        fun onGenerateClicked(tokenValues: Map<String, String>, templateId: String)
    }

    private lateinit var contentPanel: JPanel
    private lateinit var paramsPanel: JPanel
    private lateinit var templateChooser: JComboBox<*>

    private var tokenTextFields: Map<String, JTextField>? = null

    init {
        init()
        templateChooser.model = TemplateComboBoxModel(templates)
        populateTokenInputs(templates.first().tokens)

        templateChooser.addActionListener {
            if (it.actionCommand == "comboBoxChanged") {
                populateTokenInputs(templates[templateChooser.selectedIndex].tokens)
            }
        }
    }

    private fun populateTokenInputs(tokens: List<Token>) {
        paramsPanel.removeAll()
        paramsPanel.layout = GridLayoutManager(tokens.size + 1, 2)

        val fields = mutableMapOf<String, JTextField>()

        tokens.forEachIndexed { index, token ->
            paramsPanel.add(JLabel().apply {
                text = token.name
            }, createConstraints(
                row = index,
                column = 0,
                preferredSize = Dimension(50, 30)
            ))

            paramsPanel.add(JTextField().apply {
                preferredSize = Dimension(150, 30)
                fields[token.id] = this
            }, createConstraints(
                row = index,
                column = 1,
                fill = FILL_HORIZONTAL,
                horizontalSizePolicy = SIZEPOLICY_CAN_GROW or SIZEPOLICY_WANT_GROW,
                preferredSize = Dimension(150, 30)
            ))
        }

        paramsPanel.add(Spacer(),
            createConstraints(
                row = fields.size,
                column = 0,
                fill = FILL_VERTICAL,
                horizontalSizePolicy = SIZEPOLICY_CAN_SHRINK,
                verticalSizePolicy = SIZEPOLICY_CAN_GROW or SIZEPOLICY_WANT_GROW
            ))

        invokeLater { fields.entries.firstOrNull()?.value?.requestFocusInWindow() }
        tokenTextFields = fields

        paramsPanel.revalidate()
        paramsPanel.repaint()
    }

    override fun doOKAction() {
        val selectedTemplate = templateChooser.selectedItem as Template
        val tokenValues = tokenTextFields?.map {
            it.key to it.value.text
        }?.toMap() ?: throw IllegalStateException("Tokens are not initialized")

        if (tokenValues.any { it.value.isBlank() }) {
            JOptionPane.showMessageDialog(null, "All fields are required")
            return
        }

        super.doOKAction()
        listener.onGenerateClicked(tokenValues, selectedTemplate.id)
    }

    override fun createCenterPanel(): JComponent? = contentPanel

    private fun createConstraints(
        row: Int,
        column: Int,
        fill: Int = FILL_NONE,
        horizontalSizePolicy: Int = SIZEPOLICY_FIXED,
        verticalSizePolicy: Int = SIZEPOLICY_FIXED,
        preferredSize: Dimension? = null
    ) = GridConstraints(
        row,
        column,
        1,
        1,
        0,
        fill,
        horizontalSizePolicy,
        verticalSizePolicy,
        null,
        preferredSize,
        null
    )
}
