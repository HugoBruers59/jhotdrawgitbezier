/*
 * @(#)SVGDrawingPanelSample.java
 *
 * Copyright (c) 2009-2010 The authors and contributors of JHotDraw.
 *
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.mini;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.io.InputFormat;
import org.jhotdraw.draw.io.OutputFormat;
import org.jhotdraw.gui.BackgroundTask;
import org.jhotdraw.gui.Worker;
import org.jhotdraw.gui.filechooser.ExtensionFileFilter;
/**
 * Example showing how to embed the {@link org.jhotdraw.samples.svg.SVGDrawingPanel} into an application
 * that does not make use of the JHotDraw application framework.
 * <p>
 * In this case, the application consists of a JFrame with a File menu.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class SVGDrawingPanelSample extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    /** Holds the file chooser for opening a file. */
    private JFileChooser openChooser;
    /** Holds the file chooser for saving a file. */
    private JFileChooser saveChooser;
    /** Maps file filters to input formats, so that we can
     * determine the input format that the user selected for
     * opening a drawing.
     */
    private HashMap<javax.swing.filechooser.FileFilter, InputFormat> fileFilterInputFormatMap;
    /** Maps file filters to output formats, so that we can
     * determine the output format that the user selected for
     * opening a drawing.
     */
    private HashMap<javax.swing.filechooser.FileFilter, OutputFormat> fileFilterOutputFormatMap;
    /** Holds the currently opened file. */
    private File file;
    /** Creates new form SVGDrawingPanelSample */
    public SVGDrawingPanelSample() {
        initComponents();
        setSize(new Dimension(600, 400));
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        svgPanel = new org.jhotdraw.samples.svg.SVGDrawingPanel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        FormListener formListener = new FormListener();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(svgPanel, java.awt.BorderLayout.CENTER);
        fileMenu.setText("File");
        openMenuItem.setText("Open...");
        openMenuItem.addActionListener(formListener);
        fileMenu.add(openMenuItem);
        saveAsMenuItem.setText("Save As...");
        saveAsMenuItem.addActionListener(formListener);
        fileMenu.add(saveAsMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        pack();
    }
    // Code for dispatching events from components to event handlers.
    private class FormListener implements java.awt.event.ActionListener {
        FormListener() {}
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            if (evt.getSource() == openMenuItem) {
                SVGDrawingPanelSample.this.open(evt);
            }
            else if (evt.getSource() == saveAsMenuItem) {
                SVGDrawingPanelSample.this.saveAs(evt);
            }
        }
    }// </editor-fold>//GEN-END:initComponents
    /** Opens a drawing from a file. */
    private void open(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_open
        JFileChooser fc = getOpenChooser();
        if (file != null) {
            fc.setSelectedFile(file);
        }
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            svgPanel.setEnabled(false);
            final File selectedFile = fc.getSelectedFile();
            final InputFormat selectedFormat = fileFilterInputFormatMap.get(fc.getFileFilter());
            new BackgroundTask() {
                @Override
                protected void construct() throws IOException {
                    svgPanel.read(selectedFile.toURI(), selectedFormat);
                }
                @Override
                protected void done() {
                    file = selectedFile;
                    setTitle(file.getName());
                }
                @Override
                protected void failed(Throwable error) {
                    error.printStackTrace();
                    JOptionPane.showMessageDialog(SVGDrawingPanelSample.this,
                            "<html><b>Couldn't open file \"" + selectedFile.getName() + "\"<br>" +
                            error.toString(), "Open File", JOptionPane.ERROR_MESSAGE);
                }
                @Override
                protected void finished() {
                    svgPanel.setEnabled(true);
                }
            }.start();
        }
    }//GEN-LAST:event_open
    /** Saves a drawing to a file. */
    private void saveAs(java.awt.event.ActionEvent evt) { //GEN-FIRST:event_saveAs
        JFileChooser fc = getSaveChooser();
        if (file != null) {
            fc.setSelectedFile(file);
        }
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            svgPanel.setEnabled(false);
            final File selectedFile;
            if (fc.getFileFilter() instanceof ExtensionFileFilter) {
                selectedFile = ((ExtensionFileFilter) fc.getFileFilter()).makeAcceptable(fc.getSelectedFile());
            } else {
                selectedFile = fc.getSelectedFile();
            }
            final OutputFormat selectedFormat = fileFilterOutputFormatMap.get(fc.getFileFilter());
            new BackgroundTask() {
                @Override
                protected void construct() throws IOException {
                    svgPanel.write(selectedFile.toURI(), selectedFormat);
                }
                @Override
                protected void done() {
                    file = selectedFile;
                    setTitle(file.getName());
                }
                @Override
                protected void failed(Throwable error) {
                    error.printStackTrace();
                    JOptionPane.showMessageDialog(SVGDrawingPanelSample.this,
                            "<html><b>Couldn't save to file \"" + selectedFile.getName() + "\"<br>" +
                            error.toString(), "Save As File", JOptionPane.ERROR_MESSAGE);
                }
                @Override
                protected void finished() {
                    svgPanel.setEnabled(true);
                }
            }.start();
        }
    }//GEN-LAST:event_saveAs
    /** Lazily creates a JFileChooser and returns it. */
    private JFileChooser getOpenChooser() {
        if (openChooser == null) {
            openChooser = new JFileChooser();
            Drawing d = svgPanel.getDrawing();
            fileFilterInputFormatMap = new HashMap<javax.swing.filechooser.FileFilter, InputFormat>();
            javax.swing.filechooser.FileFilter firstFF = null;
            for (InputFormat format : d.getInputFormats()) {
                javax.swing.filechooser.FileFilter ff = format.getFileFilter();
                if (firstFF == null) {
                    firstFF = ff;
                }
                fileFilterInputFormatMap.put(ff, format);
                openChooser.addChoosableFileFilter(ff);
            }
            openChooser.setFileFilter(firstFF);
            openChooser.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("fileFilterChanged".equals(evt.getPropertyName())) {
                        InputFormat inputFormat = fileFilterInputFormatMap.get(evt.getNewValue());
                        openChooser.setAccessory((inputFormat == null) ? null : inputFormat.getInputFormatAccessory());
                    }
                }
            });
        }
        return openChooser;
    }
    /** Lazily creates a JFileChooser and returns it. */
    private JFileChooser getSaveChooser() {
        if (saveChooser == null) {
            saveChooser = new JFileChooser();
            Drawing d = svgPanel.getDrawing();
            fileFilterOutputFormatMap = new HashMap<javax.swing.filechooser.FileFilter, OutputFormat>();
            javax.swing.filechooser.FileFilter firstFF = null;
            for (OutputFormat format : d.getOutputFormats()) {
                javax.swing.filechooser.FileFilter ff = format.getFileFilter();
                if (firstFF == null) {
                    firstFF = ff;
                }
                fileFilterOutputFormatMap.put(ff, format);
                saveChooser.addChoosableFileFilter(ff);
            }
            saveChooser.setFileFilter(firstFF);
            saveChooser.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if ("fileFilterChanged".equals(evt.getPropertyName())) {
                        OutputFormat outputFormat = fileFilterOutputFormatMap.get(evt.getNewValue());
                        saveChooser.setAccessory((outputFormat == null) ? null : outputFormat.getOutputFormatAccessory());
                    }
                }
            });
        }
        return saveChooser;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SVGDrawingPanelSample().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private org.jhotdraw.samples.svg.SVGDrawingPanel svgPanel;
    // End of variables declaration//GEN-END:variables
}
