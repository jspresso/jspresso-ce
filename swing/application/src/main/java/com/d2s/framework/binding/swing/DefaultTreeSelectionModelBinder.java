/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.d2s.framework.binding.ConnectorSelectionEvent;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorListProvider;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.IConnectorSelector;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.util.event.ISelectionChangeListener;
import com.d2s.framework.util.event.SelectionChangeEvent;
import com.d2s.framework.util.swing.SwingUtil;

/**
 * Default implementation of <code>ITreeSelectionModelBinder</code>.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultTreeSelectionModelBinder implements
    ITreeSelectionModelBinder {

  private SelectionModelListener genericSelectionModelListener;

  /**
   * Constructs a new <code>DefaultTreeSelectionModelBinder</code> instance.
   */
  public DefaultTreeSelectionModelBinder() {
    genericSelectionModelListener = new SelectionModelListener();
  }

  /**
   * {@inheritDoc}
   */
  public void bindSelectionModel(IValueConnector rootConnector, JTree tree) {
    tree.getSelectionModel().addTreeSelectionListener(
        genericSelectionModelListener);
    TreeConnectorsListener connectorsListener = new TreeConnectorsListener(
        rootConnector, tree.getSelectionModel());
    tree.getModel().addTreeModelListener(connectorsListener);
  }

  private class CollectionConnectorsSelectionListener implements
      ISelectionChangeListener {

    private IValueConnector    rootConnector;
    private TreeSelectionModel selectionModel;

    /**
     * Constructs a new <code>CollectionConnectorsSelectionListener</code>
     * instance.
     * 
     * @param rootConnector
     * @param selectionModel
     */
    public CollectionConnectorsSelectionListener(IValueConnector rootConnector,
        TreeSelectionModel selectionModel) {
      this.rootConnector = rootConnector;
      this.selectionModel = selectionModel;
    }

    /**
     * {@inheritDoc}
     */
    public void selectionChange(final SelectionChangeEvent evt) {
      SwingUtil.updateSwingGui(new Runnable() {

        public void run() {
          ICollectionConnector connector = (ICollectionConnector) evt
              .getSource();
          int[] oldSelection = evt.getOldSelection();
          int[] newSelection = evt.getNewSelection();

          if (oldSelection != null) {
            for (int i : oldSelection) {
              if (newSelection == null
                  || Arrays.binarySearch(newSelection, i) < 0) {
                selectionModel
                    .removeSelectionPath(getTreePathForConnector(connector
                        .getChildConnector(i)));
              }
            }
          }
          if (newSelection != null) {
            for (int i : newSelection) {
              if (oldSelection == null
                  || Arrays.binarySearch(oldSelection, i) < 0) {
                selectionModel
                    .addSelectionPath(getTreePathForConnector(connector
                        .getChildConnector(i)));
              }
            }
          }
        }
      });
    }

    private TreePath getTreePathForConnector(IValueConnector connector) {
      return ConnectorTreeHelper.getTreePathForConnector(rootConnector,
          connector);
    }
  }

  private static final class SelectionModelListener implements
      TreeSelectionListener {

    /**
     * Tracks changes in model selections to forward to a selectable.
     * <p>
     * {@inheritDoc}
     */
    public void valueChanged(TreeSelectionEvent e) {
      TreePath[] treePaths = e.getPaths();
      Map<ICollectionConnector, int[]> connectorSelection = new LinkedHashMap<ICollectionConnector, int[]>();
      TreeSelectionModel sm = (TreeSelectionModel) e.getSource();

      for (TreePath treePath : treePaths) {
        TreePath parentTreePath = treePath.getParentPath();
        if (parentTreePath != null) {
          IValueConnector connector = (IValueConnector) parentTreePath
              .getLastPathComponent();
          if (connector instanceof ICollectionConnectorProvider) {
            if (!connectorSelection.containsKey(connector)) {
              ICollectionConnector parentCollectionConnector = ((ICollectionConnectorProvider) connector)
                  .getCollectionConnector();
              connectorSelection.put(parentCollectionConnector, null);

              int[] rvTmp = new int[parentCollectionConnector
                  .getChildConnectorCount()];
              int n = 0;
              for (int i = 0; i < parentCollectionConnector
                  .getChildConnectorCount(); i++) {
                IValueConnector childConnector = parentCollectionConnector
                    .getChildConnector(i);
                if (sm.isPathSelected(parentTreePath
                    .pathByAddingChild(childConnector))) {
                  rvTmp[n++] = i;
                }
              }
              int[] selectedIndices = new int[n];
              System.arraycopy(rvTmp, 0, selectedIndices, 0, n);
              connectorSelection.put(((ICollectionConnectorProvider) connector)
                  .getCollectionConnector(), selectedIndices);
            }
          }
        }
      }
      // 1st pass to clear
      for (Map.Entry<ICollectionConnector, int[]> connectorIndicesPair : connectorSelection
          .entrySet()) {
        if (connectorIndicesPair.getValue() == null
            || connectorIndicesPair.getValue().length == 0) {
          ICollectionConnector connector = connectorIndicesPair.getKey();
          int[] indices = connectorIndicesPair.getValue();
          connector.setSelectedIndices(indices);
        }
      }
      // 2nd pass to set
      boolean atLeastOneSelected = false;
      for (Map.Entry<ICollectionConnector, int[]> connectorIndicesPair : connectorSelection
          .entrySet()) {
        if (connectorIndicesPair.getValue() != null
            && connectorIndicesPair.getValue().length > 0) {
          ICollectionConnector connector = connectorIndicesPair.getKey();
          int[] indices = connectorIndicesPair.getValue();
          connector.setSelectedIndices(indices);
          atLeastOneSelected = true;
        }
      }
      if (!atLeastOneSelected) {
        IValueConnector rootConnector = (IValueConnector) treePaths[0]
            .getPath()[0];
        if ((rootConnector instanceof IConnectorSelector)) {
          ((IConnectorSelector) rootConnector)
              .fireSelectedConnectorChange(new ConnectorSelectionEvent(
                  rootConnector, rootConnector));
        }
      }
    }
  }

  private class TreeConnectorsListener implements TreeModelListener {

    private CollectionConnectorsSelectionListener connectorsSelectionListener;

    /**
     * Constructs a new <code>TreeConnectorsListener</code> instance.
     * 
     * @param rootConnector
     *            the root connector of the connector hierarchy.
     * @param selectionModel
     *            the selection model of the related tree.
     */
    public TreeConnectorsListener(IValueConnector rootConnector,
        TreeSelectionModel selectionModel) {
      connectorsSelectionListener = new CollectionConnectorsSelectionListener(
          rootConnector, selectionModel);
      checkListenerRegistrationForConnector((ICollectionConnectorListProvider) rootConnector);
    }

    /**
     * {@inheritDoc}
     */
    public void treeNodesChanged(@SuppressWarnings("unused")
    TreeModelEvent e) {
      // NO-OP as of now.
    }

    /**
     * {@inheritDoc}
     */
    public void treeNodesInserted(TreeModelEvent e) {
      checkListenerRegistrationForConnector((ICollectionConnectorListProvider) e
          .getTreePath().getLastPathComponent());
    }

    /**
     * {@inheritDoc}
     */
    public void treeNodesRemoved(@SuppressWarnings("unused")
    TreeModelEvent e) {
      // NO-OP as of now.
    }

    /**
     * {@inheritDoc}
     */
    public void treeStructureChanged(TreeModelEvent e) {
      checkListenerRegistrationForConnector((ICollectionConnectorListProvider) e
          .getTreePath().getLastPathComponent());
    }

    private void checkListenerRegistrationForConnector(
        ICollectionConnectorListProvider nodeConnector) {
      for (ICollectionConnector childNodeConnector : nodeConnector
          .getCollectionConnectors()) {
        childNodeConnector
            .addSelectionChangeListener(connectorsSelectionListener);
      }
    }

  }
}
